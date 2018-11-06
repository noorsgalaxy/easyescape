# easyescape

NITK new CSE building fire evacuation App (Android)
This app guides you to the nearest exit in case of emergency
1) You need to select floors on which you currently present.
2) Then choose the locationn nearest to you.
3) The app will automatically direct you to the nearest exit.

### To Replicate our work : 

* Use google map android apk https://developers.google.com/maps/documentation/android-sdk/intro
* You need Google Maps API key, Get it from https://developers.google.com/maps/documentation/android-sdk/signup
* To add your 'own map' on top of google map use [ground overlay](https://developers.google.com/maps/documentation/android-sdk/groundoverlay)
* Add marker using Marker class of google maps [Marker API call](https://developers.google.com/maps/documentation/android-sdk/marker)
* To add path between markers use [Polyline API](https://developers.google.com/maps/documentation/android-sdk/polygon-tutorial), it takes set of points to construct a path
* You may need to find out Lat Lng of markers, you can use this third party website http://graph.latcoding.com/ to get Lat Lng of a place


