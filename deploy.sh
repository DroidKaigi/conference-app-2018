GIT_HASH=`git rev-parse --short HEAD`

if [ "${CIRCLE_BRANCH}" = 'master' ]; then
    curl -F "file=@app/build/outputs/apk/release/app-release.apk" \
        -F "token=${DEPLOY_GATE_API_KEY}" \
        -F "message=https://github.com/DroidKaigi/conference-app-2018/tree/${GIT_HASH} https://circleci.com/gh/DroidKaigi/conference-app-2018/${CIRCLE_BUILD_NUM}" \
        -F "distribution_key=aed2445665e27de6571227992d66ea489b6bdb44" \
        -F "release_note=https://github.com/DroidKaigi/conference-app-2018/tree/${GIT_HASH} https://circleci.com/gh/DroidKaigi/conference-app-2018/${CIRCLE_BUILD_NUM}" \
        https://deploygate.com/api/users/takahirom/apps
fi
