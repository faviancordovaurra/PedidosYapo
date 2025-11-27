Patch for PedidosYapo Android project

This folder contains a patch you can apply to your PedidosYapo Android project to add Retrofit integration, BuildConfig fields and an Auth interceptor.

Files included:
- `build.gradle.kts.diff` : suggested changes for `app/build.gradle.kts` (Kotlin DSL) to add dependencies and BuildConfig fields
- `RetrofitClient.kt` : Retrofit initialization
- `AuthInterceptor.kt` : OkHttp interceptor for Authorization header
- `ApiService.kt` : Example API interface
- `Producto.kt` : Example model for `Producto`
- `apply_patch.sh` : script with instructions to apply files

How to apply:
1) Copy this folder into the root of the PedidosYapo Android repo or put files in a temp folder.
2) Inspect the diffs before applying.
3) Apply Gradle changes to `app/build.gradle.kts`.
4) Copy `RetrofitClient.kt`, `AuthInterceptor.kt`, `ApiService.kt`, and `Producto.kt` under `app/src/main/java/<your-package>/network`.
5) Run Gradle sync and build.

Testing locally:
- For local emulator use `debug` build variant and `BASE_URL` set to `http://10.0.2.2:8085/` (backend running on your machine).
- For staging use `staging` build variant (update `BASE_URL` to staging URL) and run tests.

If you want me to apply the patch directly in this repo (create a PR) move the `PedidosYapo` project into the workspace and I’ll apply the patch and test it.
