# Build instructions

Right click on MainActivity and run the program.
You can run it on your own physical device if you'd like, but
if you want to use a emulator, I recommend the Pixel 2 in Android Studio
because it has a fairly large screen.

For Android Studio, I used Android Studio Flamingo | 2022.2.1



# Notes
I had trouble developing unit tests because of how the program is multithreaded and having to mock the behavior in order to unit test. 


In terms of manual testing, I did do emulation tests on the following devices without a problem:

- Pixel 2 API 27 (Emulation) - 1080 x 1920 pixels
- Nexus S API 29 (Emulation) - 480 x 800 pixels
- Samsung Galaxy S10 (Physical device) - 2280 x 1080 pixels (FHD+)

Additionally, I also verified output by pasting and then filtering the JSON data into Google Sheets and comparing that to my output in the App and it was identical.
