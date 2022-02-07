package com.waqar.tmdb.di

import com.waqar.tmdb.network.ApiService
import android.content.Context
import android.os.Build
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.waqar.tmdb.API_KEY
import com.waqar.tmdb.BuildConfig
import com.waqar.tmdb.HEADER_CACHE_CONTROL
import com.waqar.tmdb.HEADER_PRAGMA
import com.waqar.tmdb.utils.Utils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.internal.userAgent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val URL = "https://api.themoviedb.org/3/"

    @Provides
    @Singleton
    fun provideKotlinJsonAdapterFactory(): KotlinJsonAdapterFactory = KotlinJsonAdapterFactory()

    @Provides
    @Singleton
    fun provideMoshi(kotlinJsonAdapterFactory: KotlinJsonAdapterFactory): Moshi =
        Moshi.Builder()
            .add(kotlinJsonAdapterFactory)
            .build()


    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(
            moshi
        )

    @Provides
    @Singleton
    fun provideOkHttp(
        @ApplicationContext context: Context
    ): OkHttpClient {

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(CommonParamInterceptor())
            .addInterceptor(provideOfflineCacheInterceptor(context))
            .addInterceptor(UserAgentInterceptor("Tmdb/" + BuildConfig.VERSION_NAME + " (" + BuildConfig.APPLICATION_ID + "; build:" + BuildConfig.VERSION_CODE + "; android " + Build.VERSION.RELEASE + ") " + userAgent))
            .addNetworkInterceptor(provideCacheInterceptor(context))
            .cache(provideCache(context))

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor();
            logging.level = (HttpLoggingInterceptor.Level.BODY)
            httpClient.addInterceptor(logging)
            httpClient.addNetworkInterceptor(StethoInterceptor())
        }

        return httpClient.build()
    }

    class CommonParamInterceptor() : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()

            val originalHttpUrl = originalRequest.url
            val httpUrlBuilder = originalHttpUrl.newBuilder()

            httpUrlBuilder.addQueryParameter(
                API_KEY,
                "bb78e4cf3442e302d928f2c5edcdbee1"
            )

            val newHttpUrl = httpUrlBuilder.build()
            val requestBuilder = chain.request().newBuilder()
                .header("Accept", "application/json").url(newHttpUrl)
            val newRequest = requestBuilder.build()
            return chain.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache? {
        var cache: Cache? = null
        try {
            cache = Cache(File(context.cacheDir, "http-cache"), (10 * 1024 * 1024).toLong()) // 10 MB
        } catch (e: Exception) {
            Timber.e("Cache: Could not create Cache!")
        }
        return cache
    }

    @Provides
    @Singleton
    fun provideCacheInterceptor(@ApplicationContext context: Context): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val cacheControl: CacheControl = if (Utils.isInternetAvailable(context)) {
                CacheControl.Builder().maxAge(0, TimeUnit.SECONDS).build()
            } else {
                CacheControl.Builder()
                    .maxStale(1, TimeUnit.DAYS)
                    .build()
            }
            response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }


    @Provides
    @Singleton
    fun provideOfflineCacheInterceptor(@ApplicationContext context: Context): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!Utils.isInternetAvailable(context)) {
                val cacheControl: CacheControl = CacheControl.Builder()
                    .maxStale(1, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }


    class UserAgentInterceptor(val userAgentt: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder: Request.Builder = chain.request().newBuilder()

            builder.addHeader("User-Agent", userAgentt)

            return chain.proceed(builder.build());
        }
    }


    @Singleton
    @Provides
    fun getApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideRetrofitClient(
        okHttp: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(moshiConverterFactory)
        .client(okHttp)
        .baseUrl(URL)
        .client(okHttp)
        .build()
}

