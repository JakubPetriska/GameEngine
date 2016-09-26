## Releasing

1. Make sure 'CHANGELOG.md' is up-to-date and contains all changes in the version being released.
2. Make sure 'gradle.properties' contains correct version.
3. Merge to master.
4. 'git tag -a X.Y.Z -m "X.Y.Z"` (where X.Y.Z is the new version).
5. Generate javadoc with following parameters:

Custom scope: 'src[engine]:com.jakubpetriska.gameengine.api..*||src[android-adapter]:com.jakubpetriska.gameengine.api.android..*'

Other command line arguments: '-encoding utf-8 -bootclasspath /path/to/sdk/platforms/android-##/android.jar'

or in case of Windows something like this: '-encoding utf-8 -bootclasspath C:\Users\Jakub\Android\sdk\platforms\android-22\android.jar'

6. Update javadoc on the website with the newly updated one.
7. Update website information according to changes in the new version.
8. Update 'gradle.properties' with next version.
9. Add next version to 'CHANGELOG.md'.
