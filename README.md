# Build instructions

Right click on [MainActivity class](https://github.com/gcmaidana/fetch-exercise/blob/master/app/src/main/java/com/example/fetch_exercise/MainActivity.java) and run the program in Android Studio.
You can run it on your own physical device if you'd like, but
if you want to use a emulator, I recommend the Pixel 2 in Android Studio
because it has a fairly large screen.

For Android Studio, I used Android Studio Flamingo | 2022.2.1
- JDK 17.
- SDK 33.

# Notes

I tried making the code as modular as possible, which is why one class deals mostly with UI and another class deals with the JSON data. It is multithreaded since Android doesn't allow you to do network operation on the UI thread. I have two functions in the JSON class that are important: one that fetches the data, and one that sorts the data. These two threads are sequential in order to not have any data inconsistencies/errors. The method that fetches the JSON data opens a thread and handles that, then at the end of that method, the sort function is called and the jsonArray is passed in as a parameter. I did it this way because if I call both threads in MainActivity, then they can run concurrently and that can cause problems with data inconsistency.

# Testing Notes
I had trouble developing unit tests for my methods because of how the program is multithreaded and having to mock the behavior in order to unit test. I did try to mock the behavior using Mockito and I also tried with Robolectric but I didn't have any success.


In terms of manual testing, I did do emulation tests on the following devices that target the current OS release (Android 13) without a problem:

- Pixel 2 API 33 (Emulation) - 1080 x 1920 pixels
- Nexus S API 33 (Emulation) - 480 x 800 pixels
- Samsung Galaxy S10 (Physical device) - 2280 x 1080 pixels (FHD+)

Additionally, I also verified output by pasting and then filtering the JSON data into Google Sheets and comparing that to my output in the App and it was identical.


![Screenshot1](sdkscreenshot.png)

![Screenshot2](emulator.png)
