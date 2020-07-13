/*Junior Green
 * Mr.Bulhao
 * ICS4U1
 * 25 October 2019
 */
package application;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class Main extends Application {

	private Button btnStart, btnSubmit, btnQuit; //Declare button objects 
	private Label lblMessage; //Declare label message 
	int ranNum = 0, increment = 0, rowNum = 14, buttonIndex = 0, colorCorrect = 0, locationCorrect = 0, duplicateCount = 0; // Declare int variables
	Button[][] buttons = new Button[15][4]; //Declare and initialize a 2D Button Array
	int[][] colorMod = new int[15][4]; //Declare and initializes a 2D array which keep track of the color of every button
	Label[] labels = new Label[15]; //Declare and initialize an array of Label Objects 
	ArrayList<String> patternNames, colorNames; //Declare String ArrayLists
	ArrayList<Color> pattern, colors, colorSelection; //Declare Color ArrayLists
	Alert win, playAgain, quit, error, lose; // Declare Alert objects 
	boolean errorBool = false; //Declare and initialize a boolean object
	Random rnd = new Random(); //Declare and initialize a Random object

	public void start(Stage primaryStage) {
		try {
 
			//Changes all the values in the colorMod array to -1
			for (int rows = 0; rows < colorMod.length; rows++) {
				for (int cols = 0; cols < colorMod[0].length; cols++) {
					colorMod[rows][cols] = -1;
				}
			}
			//Initializes all ArrayLists
			colorSelection = new ArrayList<Color>();
			colors = new ArrayList<Color>();
			pattern = new ArrayList<Color>();
			patternNames = new ArrayList<String>();
			colorNames = new ArrayList<String>();

			Image imgLogo = new Image("file:mastermind.png");
			ImageView iviewLogo = new ImageView(imgLogo);
			iviewLogo.setFitWidth(300);

			playAgain = new Alert(AlertType.CONFIRMATION); //Initialize alert object*
			playAgain.setHeaderText(null); //Sets header text*
			playAgain.setTitle("Mastermind"); //Sets title*
			playAgain.getButtonTypes().clear(); //Clears current button*
			playAgain.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO); //Adds YES and NO buttons*
			playAgain.setContentText("Would you like to play again?"); //Sets text insides alert*

			quit = new Alert(AlertType.CONFIRMATION);//*
			quit.setHeaderText(null);//*
			quit.setTitle("MasterMind");//*
			quit.setContentText("Are you sure you want to quit?");//*
			quit.getButtonTypes().clear();//*
			quit.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);//*

			error = new Alert(AlertType.ERROR);//*
			error.setHeaderText(null);//*
			error.setTitle("Error");//*
			error.setContentText("You must select (4) colors before selecting SUBMIT!");//*

			btnStart = new Button();
			btnStart.setText("START");
			btnStart.setPrefSize(90, 30);
			btnStart.setFocusTraversable(false);
			btnStart.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent arg0) {
					lblMessage.setText("Select four (4) colours"); //Changes text displayed in label object
					reset(3);//Executes method 
					resetBoard(true);//Executes method
					btnStart.setDisable(true); //Disables start button
					btnSubmit.setDisable(false); //Enables submit button
					reset(0);//Executes method
					
//DISPLAYS "Code"	//System.out.println(patternNames.get(0) + ", " + patternNames.get(1) + ", " + patternNames.get(2) + ", " + patternNames.get(3));
				}
			});
			btnSubmit = new Button(); 
			btnSubmit.setText("SUBMIT");
			btnSubmit.setPrefSize(90, 30);
			btnSubmit.setFocusTraversable(false);
			btnSubmit.setDisable(true);
			btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					try {
						for (Color col : pattern) { //Enhanced for loop which runs through Array List that contains "Code"
							for (int cols = 0; cols < buttons[0].length; cols++) { 
								if (col == colors.get(colorMod[rowNum][cols])) {
									colorSelection.add(col); //If the color selected is in the pattern add to array list

									if (pattern.indexOf(col) == cols) {
										locationCorrect += 1; //If the color is selected is in the pattern and is in the same column increment 
									}
								}
							}
						}
						for (int index0 = 0; index0 < colorSelection.size(); index0++) {
							for (int index = 0; index < colorSelection.size(); index++) {
								if (colorSelection.get(index0) == colorSelection.get(index)) {
									duplicateCount++; //Increments based on how many duplicate colors there are 
								}
							}
							if (duplicateCount > 1) {
								for (int index2 = 0; index2 < duplicateCount - 1; index2++) {
									colorSelection.remove(colorSelection.indexOf(colorSelection.get(index0))); //Removes all duplicates if there are any
								}
							}
							duplicateCount = 0; //Resets variable 
						}
						
						colorCorrect = colorSelection.size(); //Sets variable based on how many colors were guessed correct
						errorBool = false; //If Exception isn't thrown sets variable to false to enable further exect
						
					} catch (ArrayIndexOutOfBoundsException e2) {
						error.showAndWait(); //If error thrown show error
						colorCorrect = 0; //Resets variable
						locationCorrect = 0; //Resets variable
						errorBool = true; //Sets boolean to true to prevent further execution
					}
					if (errorBool == false) {
						
						//Disables current row and enables the next row
						for (int cols = 0; cols < buttons[0].length; cols++) {
							buttons[rowNum][cols].setDisable(true);
							if (rowNum != 0)
								buttons[rowNum - 1][cols].setDisable(false);
						}

						labels[rowNum].setText(String.valueOf(colorCorrect) + ", " + String.valueOf(locationCorrect)); //Outputs results
						lblMessage.setText(String.valueOf(colorCorrect) + " colour(s) correct\n" + String.valueOf(locationCorrect) + " color(s) in the correct location"); //Displayes results in label

						if (colorCorrect == 4 && locationCorrect == 4) {
							win = new Alert(AlertType.INFORMATION); //*
							win.setTitle("You win!");//*
							win.setHeaderText(null);//*
							win.setContentText("Congratulations... you cracked the code! \n It took you " + String.valueOf(15 - rowNum) + " attempt(s).");//*
							win.showAndWait();//*
							
							Optional<ButtonType> result = playAgain.showAndWait();

							if (result.get() == ButtonType.YES) {
								reset(3); //Executes method
								resetBoard(false); //Executes method
							} else {
								//Terminates program
								Platform.exit();
								System.exit(0);
							}
						}

						rowNum -= 1; //Decrements rowNum variable
						colorCorrect = 0; //Reset variable
						locationCorrect = 0; //Reset variable
						colorSelection.clear(); //Clears Array list that holds the users colorSelection

						if (rowNum < 0) {
							lose = new Alert(AlertType.WARNING); //*
							lose.setHeaderText(null);//*
							lose.setTitle("You lose!");//*
							lose.setContentText("GAME OVER!\nYou failed to crack the code!\n\nThe code was "
									+ patternNames.get(0) + ", " + patternNames.get(1) + ", " + patternNames.get(2)
									+ ", " + patternNames.get(3)); //*
							lose.showAndWait(); //Displays alert

							Optional<ButtonType> result = playAgain.showAndWait(); //Displays playAgain alert
							
							if (result.get() == ButtonType.YES) //Resets game
							{
								reset(3);
								resetBoard(false);
								rowNum -= 1;
								colorCorrect = 0;
								locationCorrect = 0;
							} else //Terminate program
							{
								Platform.exit();
								System.exit(0);
							}
						}
					}
				}
			});
			btnQuit = new Button();
			btnQuit.setText("QUIT");
			btnQuit.setPrefSize(90, 30);
			btnQuit.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e2) {

					Optional<ButtonType> result = quit.showAndWait();

					//Terminates program if Yes otherwise, no.
					if (result.get() == ButtonType.YES) {
						Platform.exit();
						System.exit(0);
					} else
						e2.consume();
				}
			});
			lblMessage = new Label();
			lblMessage.setPrefSize(300, 40);
			lblMessage.setAlignment(Pos.CENTER);
			lblMessage.setStyle("-fx-font-weight: bold");
			lblMessage.setText("Click START to begin");

			HBox hboxPane = new HBox(10);
			hboxPane.getChildren().addAll(btnStart, btnSubmit, btnQuit);

			FlowPane root = new FlowPane();
			root.setPadding(new Insets(10, 50, 10, 50));
			root.setVgap(5);
			root.setHgap(5);
			root.setAlignment(Pos.TOP_CENTER);
			root.getChildren().add(iviewLogo);
			for (int rows = 0; rows < buttons.length; rows++) {
				for (int cols = 0; cols < buttons[0].length; cols++) {
					buttons[rows][cols] = new Button(); //Initializes button
					buttons[rows][cols].setPrefSize(35, 35);
					buttons[rows][cols].setStyle("-fx-border-color: black; -fx-control-inner-background: light grey");
					buttons[rows][cols].setDisable(true); //Disables button
					buttons[rows][cols].setOnMouseClicked(new EventHandler<MouseEvent>() {
						public void handle(MouseEvent e) {
							if (e.getButton() == MouseButton.SECONDARY) {
								for (int cols = 0; cols < buttons[rowNum].length; cols++) {
									if (e.getSource() == buttons[rowNum][cols]) //Finds column of button clicked using linear search algorithm
									{
										buttonIndex = cols;
										break;
									}
								}
								if (colorMod[rowNum][buttonIndex] == 0 || colorMod[rowNum][buttonIndex] == -1) //Jumps to end of color array
								{
									colorMod[rowNum][buttonIndex] = 5;
								} 
								else //Goes one spot down color array
								{
									colorMod[rowNum][buttonIndex] -= 1;
								}
								buttons[rowNum][buttonIndex].setBackground(new Background(
										new BackgroundFill(colors.get(colorMod[rowNum][buttonIndex]), null, null))); //Changes background based on ColorMod value
							} else if (e.getButton() == MouseButton.PRIMARY) {
								for (int cols = 0; cols < buttons[rowNum].length; cols++) {
									if (e.getSource() == buttons[rowNum][cols]) {
										buttonIndex = cols;
										break;
									}
								}
								if (colorMod[rowNum][buttonIndex] == 5) //Jumps to start of array
								{
									colorMod[rowNum][buttonIndex] = 0;
								}
								else //Goes one spot up in color array
								{
									colorMod[rowNum][buttonIndex] += 1;
								}

								buttons[rowNum][buttonIndex].setBackground(new Background(
										new BackgroundFill(colors.get(colorMod[rowNum][buttonIndex]), null, null))); //Changes background
							}
						}
					});
					root.getChildren().add(buttons[rows][cols]);
					
					if (cols == buttons[0].length - 1) //Adds label to end of the current row after all buttons have been outputted 
					{ 
						labels[rows] = new Label();
						labels[rows].setPrefSize(35, 35);
						labels[rows].setAlignment(Pos.CENTER);
						labels[rows].setStyle("-fx-border-color: black; -fx-control-inner-background: white");
						root.getChildren().add(labels[rows]);
					}
				}
			}
			root.getChildren().addAll(hboxPane, lblMessage);

			Scene scene = new Scene(root, 330, 810);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent e) {

					Optional<ButtonType> result = quit.showAndWait();

					if (result.get() == ButtonType.YES) //Terminates program if user clicks yes
					{
						Platform.exit();
						System.exit(0);
					} else //Returns back to scene
						e.consume();
				}
			});
			primaryStage.setTitle("Mastermind"); //Sets title of window
			primaryStage.setScene(scene); //sets current scene
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reset(int f) //Clears and adds all colors and color names to it's respective array list (f = trigger both or one of them)
	{
		if (f == 0 || f == 3) //Clears array list and adds all colors
		{
			colors.clear();
			colors.add(Color.RED);
			colors.add(Color.BLUE);
			colors.add(Color.GREEN);
			colors.add(Color.YELLOW);
			colors.add(Color.PINK);
			colors.add(Color.CYAN);
		}
		if (f == 1 || f == 3) //Clears array list and add color strings
		{
			colorNames.clear();
			colorNames.add("RED");
			colorNames.add("BLUE");
			colorNames.add("GREEN");
			colorNames.add("YELLOW");
			colorNames.add("PINK");
			colorNames.add("CYAN");
		}
	}

	//Public method that resets all variables, clears buttons and goes back to first row (start = first execution of method or not)
	public void resetBoard(boolean start) {
		lblMessage.setText("Select four (4) colours"); //Changes text displayed in label object
		if (start == true) {
			reset(3);
			rowNum = 14;
		} else
			rowNum = 15;

		increment = 0;
		pattern.clear();
		patternNames.clear();

		for (int i = 0; i < 4; i++) {
			ranNum = rnd.nextInt(5 - increment);
			pattern.add(colors.get(ranNum));
			patternNames.add(colorNames.get(ranNum));
			colorNames.remove(ranNum);
			colors.remove(ranNum);
			increment++;
		}
		if (start == false)
			reset(0);

		//Resets the background colors 
		for (int rows = 0; rows < buttons.length; rows++) {
			for (int cols = 0; cols < buttons[0].length; cols++) {
				if (rows == 14) {
					buttons[rows][cols].setDisable(false);
					buttons[rows][cols].setBackground(new Background(new BackgroundFill(null, null, null)));

				} else {
					buttons[rows][cols].setDisable(true);
					buttons[rows][cols].setBackground(new Background(new BackgroundFill(null, null, null)));
				}
			}
		}
		for (int rows = 0; rows < colorMod.length; rows++) {
			for (int cols = 0; cols < colorMod[0].length; cols++) {
				colorMod[rows][cols] = -1;
			}
		}

		//Reset all label objects
		for (int rows = 0; rows < labels.length; rows++) {
			labels[rows].setText("");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}