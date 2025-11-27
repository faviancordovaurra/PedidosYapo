#!/usr/bin/env bash
set -e

echo "This script provides instructions to apply the Android integration patch to PedidosYapo project."

echo "1) Add dependencies & BuildConfig fields:"
echo "   - Open app/build.gradle.kts and add the dependencies above (see build.gradle.kts.diff) and BuildConfig fields."

echo "2) Add Kotlin files to project package (change package path if necessary):"
echo "   - Copy files from this folder to your Android app package under app/src/main/java/<YOUR_PACKAGE>/network/"

echo "3) TokenStorage: update usage of TokenStorage.getToken(context) access in your login flows and store token using TokenStorage.saveToken(context, token)"

echo "4) Retrofit usage example (in Activity or ViewModel):"
cat <<'EOF'
val apiService = RetrofitClient.getClient(requireContext()).create(ApiService::class.java)
apiService.getProductos().enqueue(object : Callback<List<Producto>> {
    override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
        // handle
    }
    override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
        // handle
    }
})
EOF

echo "5) Run gradle sync and build the project. If the project uses a different package name than com.pedidosyapo, update the packages in the Kotlin files accordingly."

echo "6) Test locally: run your backend (staging or local), use 10.0.2.2 for emulator or staging URL in BASE_URL for the staging build type."

echo "Done. If you want, I can create a PR if you place this project inside this workspace." 
