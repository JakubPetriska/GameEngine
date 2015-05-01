# Monolith Game Engine
Game engine for Android.

## v0.3 - IN PROGRESS
* Collider group is now a string name of the group so there can be any number of collider groups.
* TouchInput fixed.
* Documentation edits.
* Better explanation of problem when engine throws an exception.

## v0.2.1
* Static and dynamic collider groups added.
* Support extended to Android 2.2.
* Missing Messenger documentation added.

## v0.2
* Rotation and scale added.
* Support of custom models in obj format.
* Collisions added. Currently supports only box collider.
* GameObject now has tag. This allows recognizing the object for example during collision.
* Camera component added. Has near and far parameters that can be set through code or xml.
* Mesh component renamed to Model.
* Transform element and it's parts are no longer mandatory in scene definition xml file.
* Coordinate system changed to left handed with z pointing forward, y up and x to the right.
* LWJGL util library removed. Engine uses it's own vector and matrix class.
* Param removed from scene definition xm files. Params are listed directly in component tag.
* Allowed script parameter types are now boolean, int, long, float, double or their respective wrapper classes.
* API for retrieval of device's display parameters added.
* Primitive cube size changed to 1.

## v0.1
* First version.
* Supports basic features.
* Objects can be only moved not rotated or scaled.
* Camera and lighting is immovable.