# Ignore inline messages which lay outside a diff's range of PR
github.dismiss_out_of_range_messages

# ktlint
checkstyle_format.base_path = Dir.pwd

checkstyle_format.report "app/build/reports/ktlint/ktlint-#{ENV['APP_BUILD_TYPE'].downcase}.xml"

# AndroidLint
android_lint.report_file = "app/build/reports/lint-results-#{ENV['APP_BUILD_TYPE'].downcase}.xml"
android_lint.skip_gradle_task = true
android_lint.severity = "Error"
android_lint.lint(inline_mode: true)
