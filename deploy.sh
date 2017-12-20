GIT_HASH=`git rev-parse --short HEAD`

curl -F "file=@app/build/outputs/apk/debug/app-release.apk" -F "token=${DEPLOY_GATE_API_KEY}" -F "message=https://github.com/DroidKaigi/conference-app-2017/tree/${GIT_HASH} https://circleci.com/gh/DroidKaigi/conference-app-2017/${CIRCLE_BUILD_NUM}" -F "distribution_key=55e453187e765b251bf485d2e81d7bd22aa6aca9" -F "release_note=https://github.com/DroidKaigi/conference-app-2017/tree/${GIT_HASH} https://circleci.com/gh/DroidKaigi/conference-app-2017/${CIRCLE_BUILD_NUM}" https://deploygate.com/api/users/konifar/apps
