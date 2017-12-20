GIT_HASH=`git rev-parse --short HEAD`

curl -F "file=@app/build/outputs/apk/release/app-release.apk" -F "token=${DEPLOY_GATE_API_KEY}" -F "message=https://github.com/DroidKaigi/conference-app-2018/tree/${GIT_HASH} https://www.bitrise.io/build/${BITRISE_BUILD_NUMBER}" -F "distribution_key=140fd4476a5dbe9d8bcb3ccec26172ee3e27e2cf" -F "release_note=https://github.com/DroidKaigi/conference-app-2018/tree/${GIT_HASH} https://www.bitrise.io/build/${BITRISE_BUILD_NUMBER}" https://deploygate.com/api/users/takahirom/apps
