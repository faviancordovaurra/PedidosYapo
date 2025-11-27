# Android Integration Guide (Retrofit + BuildConfig + Auth)

This guide provides a step-by-step for integrating the Android app with this backend and staging environment.

## 1) BuildConfig (Gradle)
Add the following to your `app/build.gradle` inside `android { ... }` -> `buildTypes`:

```
buildTypes {
    debug {
        buildConfigField "String", "BASE_URL", '"http://10.0.2.2:8085/"'
    }
    staging {
        initWith debug
        buildConfigField "String", "BASE_URL", '"https://staging.example.com/"'
    }
    release {
        buildConfigField "String", "BASE_URL", '"https://api.example.com/"'
    }
}
```

## 2) Dependencies
Add to `build.gradle` (Module: app)
```
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:okhttp:4.9.3'
implementation 'com.squareup.okhttp3:logging-interceptor:4.9.3'
```

## 3) Retrofit client and Auth Interceptor (example)
Create `RetrofitClient.java`:
```java
public class RetrofitClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(new AuthInterceptor())
                .build();

            retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        }
        return retrofit;
    }
}
```

Create `AuthInterceptor.java` (for Bearer token):
```java
public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        // get your token from secure storage
        String token = TokenStorage.getToken();
        Request.Builder builder = original.newBuilder();
        if (token != null && !token.isEmpty()) {
            builder.header("Authorization", "Bearer " + token);
        }
        Request request = builder.build();
        return chain.proceed(request);
    }
}
```

## 4) Create `ApiService` interface
```java
public interface ApiService {
    @GET("api/productos")
    Call<List<Producto>> getProductos();

    @Multipart
    @POST("api/productos/{id}/upload")
    Call<ResponseBody> uploadImage(@Path("id") long id, @Part MultipartBody.Part file);
}
```

## 5) Using the ApiService
```java
ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
Call<List<Producto>> call = apiService.getProductos();
```

## 6) Testing locally
- When running in the Android emulator, `10.0.2.2` maps to host `localhost`. Use the `debug` or `staging` build variant.
- For a physical device, point `BASE_URL` to a staging server reachable by IP (or use `ngrok`).

## 7) Notes
- Keep tokens in secure storage (EncryptedSharedPreferences or Keystore)
- Use `staging` build variant to test pre-production with the `staging` backend
- For file upload, ensure you set `@Part MultipartBody.Part file` and `Content-Type` properly

If you want, I can generate a patch to modify the `petschop` project with these files and updates to Gradle. Provide the project inside the workspace or let me create a bundle that you can apply.

---
Patch for PedidosYapo (Android)
I created a small patch bundle under `android-integration/pedidoyapo-patch/` with all the files you need to integrate Retrofit, BuildConfig, and AuthInterceptor.

Follow `README-apply-patch.md` inside the patch folder for step-by-step instructions or, if you prefer, copy the files and I can apply them directly if you add the PedidosYapo project to the workspace.