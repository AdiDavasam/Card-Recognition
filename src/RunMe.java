import core.DisplayWindow;

public class RunMe {
    public static void main(String[] args) {
        // --== Load an image to filter ==--
        DisplayWindow.showFor("images/Cards_Image_1.jpeg", 800, 600, "CardFilter");
//        DisplayWindow.showFor("images/Cards_Image_1.jpeg", 800, 600, "DoNothingFilter");

        // --== Determine your input interactively with menus ==--
//        DisplayWindow.getInputInteractively(800,600);
    }
}
