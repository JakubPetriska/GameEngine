Monolith engine library

Library is meant to be used with Android Studio. It may work in other IDEs as well.

To use this in your project you need to create new application module containing the library.

First you need to include the .aar file to your project. 
One way to do this in Android Studio is to go to File -> New -> New module, here select Import .JAR or .AAR package
and browse your computer for the .aar file and click finish.

Next you need to include the .jar file. It is recommended to add this file to newly created module, 
into it's root folder. Then you need to add it to module's build.gradle file.
Append this line to the end of the file:
		
		artifacts.add("default", file('monolith_core_v0.1.jar'))
		
Make sure you replace the name of the file with the name of the actual file you are using.
