Test app

In order to provide the credentials, add the following variables to your environment:

export frogmi_key=$someKey
export frogmi_company_id=$someID

Alternatively, you can set them up manually in the app/build.gradle.kts file, 
or the FrogApplication file & the test files.

You will have to reopen android studio/IntelliJ after this and build the app. Alternatively, 
after setting the variables, you can run:

./gradlew clean build --refresh-dependencies app:installDebug

if a release variant is desired, use the following command:

./gradlew clean build --refresh-dependencies app:installRelease

I avoided most android specific tools to make it easier to read for non-android devs.

The entry point is MainActivity. no UseCases, since the logic is simple.

A test debug key is included in the repository, in case a release build is needed for some reason.

A slight fading effect indicates when a new page has been added to the list.
