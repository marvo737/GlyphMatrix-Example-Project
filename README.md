Glyph Matrix Example Project
====================



About the Demo
--------------
This example project contains multiple toy demos:
- `basic` demo which shows the application icon
    - `Touch-down` (press down) to increment and display a counter
    - `Touch-up` (release) to stop incrementing the counter
- `glyphbutton` demo which shows a randomly populated grid
    - `Long-press` the `Glyph Button` on the device to generate a new random grid
- `animation` demo which shows an indefinite animation until the toy is deactivated

After going through the `Setup` stage in this document the demo project can be run on the device.
> Tip: `Short-press` the `Glyph Button` to navigate between the toys.

The demo project already contains the necessary libraries (GlyphMatrix SDK) and source structure as an example. However, if you want to install libraries for your own application, please reference the SDK documentation.

These examples are written in Kotlin and also provide a useful Kotlin wrapper around the original SDK that you can use in your own project.


Requirements
--------------
Android Studio, Kotlin, compatible device with Glyph Matrix

Setup
-----------------------
1.Prepare your Nothing device and connect it to the device for development

2.Clone this project or download this repository as a ZIP uncompressed your local directory.

3.Open a new windows in Android studio and hit file on the menu bar, select open.
![Open Project](images/open.png)

4.Select the directory where you have cloned the repository or unzipped the files and click `Open`

![Select Project](images/select.png)


5.Once the Gradle files have been synced and your phone is connected properly, you should see your device name shown at the top and a play button. Click the play button to install this example project.

![Run Project](images/run.png)




Running a Toy
------------
When a the example project is installed on the device, toys within the project needs to be acivated before it can be used.

Open the `Glyph Interface` app from your device settigns.

Tap on the `edit` button to move toys to the enabled state.

![Disabled Toys](images/toy_carousoul.png)

Use the handle bars to drag a toy from `Disabled` to `Active` state.

![Moving Toys](images/toy_disable.png)

The toys should now be in the `Active` state, and can be viewed on the Glyph Matrix using Glyph Tocuh

![Active Toys](images/toy_active.png)

