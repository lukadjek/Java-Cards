package main;

/**
 * 
 * @author Luka
 *	Class name: Player
 *	Class job: this class is used for testing. It can load a file with a set of questions and answers.
 *	Furthermore, it should provide a simple and intuitive GUI for a user to test his/her knowledge 
 *	It also has two menu options for creating a new card set and exiting the playground.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Player extends JFrame {

	private JFrame frame;
	private JTextArea txtArea;
	private JMenuItem createMenuItem;
	private JMenuItem loadMenuItem;
	private ArrayList<Card> cardList;
	private JButton btnShow;
	private File myFile;
	private JLabel currentFileName;
	private int currentCardIndex;
	private boolean questionShown = false;

	public static void main(String[] args) {
		
			// make everything here clear, concise and understandable with each method doing only one thing (good OOP) :D
		Player builder = new Player();
		builder.setUp();
		builder.addComponents();
		builder.finalTouch();

	}

	private void setUp() {	// general set up 
		frame = new JFrame("Player");
		frame.setSize(530, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void addComponents() {	// components/visual stuff

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		createMenuItem = new JMenuItem("Create new card set");
		loadMenuItem = new JMenuItem("Load card set");
		JMenuItem exitMenuItem = new JMenuItem("Exit");

		createMenuItem.addActionListener(new CreateActionListener());
		loadMenuItem.addActionListener(new LoadActionListener());
		exitMenuItem.addActionListener(new ExitActionListener());
		
		KeyStroke createMenuItem_shortcut = KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.CTRL_DOWN_MASK);
		createMenuItem.setAccelerator(createMenuItem_shortcut);
		
		KeyStroke loadMenuItem_shortcut = KeyStroke.getKeyStroke(KeyEvent.VK_9, KeyEvent.CTRL_DOWN_MASK);
		loadMenuItem.setAccelerator(loadMenuItem_shortcut);
		
		KeyStroke exitMenuItem_shortcut = KeyStroke.getKeyStroke(KeyEvent.VK_0, KeyEvent.CTRL_DOWN_MASK);
		exitMenuItem.setAccelerator(exitMenuItem_shortcut);

		fileMenu.add(createMenuItem);
		fileMenu.add(loadMenuItem);
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);

		Font bigFont = new Font("sanserif", Font.BOLD, 15);

		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(new Color(22, 144, 123));
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

		JLabel lblLoadedQuestion = new JLabel("Test your knowledge");
		lblLoadedQuestion.setForeground(Color.white);
		txtArea = new JTextArea(10, 30);
		txtArea.setFont(bigFont);
		txtArea.setLineWrap(true);
		txtArea.setWrapStyleWord(true);
		txtArea.setEditable(false);
		txtArea.setBackground(Color.gray);
		txtArea.setForeground(Color.white);
		txtArea.addKeyListener(new MyKeyAdapter());

		JScrollPane answerScroller = new JScrollPane(txtArea);
		answerScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		answerScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		btnShow = new JButton("Show Question");
		Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
		btnShow.setCursor(handCursor);
		btnShow.setPreferredSize(new Dimension(140, 40));
		btnShow.addActionListener(new ShowActionListener());
		
		currentFileName = new JLabel();
		cardList = new ArrayList<Card>();

		mainPanel.add(lblLoadedQuestion);
		mainPanel.add(answerScroller);
		mainPanel.add(btnShow);
		mainPanel.add(currentFileName);

	}

	private void finalTouch() {	// center the frame on the screen

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private class CreateActionListener implements ActionListener {	// open up the QuizCardBuilder form

		@Override
		public void actionPerformed(ActionEvent e) {
			new Builder();
		}
	}
	
	private class MyKeyAdapter extends KeyAdapter {	// make it easier for the user to add cards with focus and enter key 
		@Override
		public void keyPressed(KeyEvent e) {
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
				btnShow.doClick();
			}
		}
	}

	private class LoadActionListener implements ActionListener {	// open up the save dialog form
		
		private JFileChooser fileChooser;

		@Override
		public void actionPerformed(ActionEvent e) {
			
				// make the default location be the user's desktop
			String openLocation = System.getProperty("user.home") + "/Desktop";
			
			fileChooser = new JFileChooser(openLocation);
		
				// add some more information messages for the user
			fileChooser.setDialogTitle("Select .txt file");
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file only", "txt");
			fileChooser.setFileFilter(filter);
            
                      
           // perform all the necessary checks and stop if something failed, otherwise continue with file reading
           if ( !checkup() )
        	   return;
         		
			cardList.clear();	// clear the card list
			currentCardIndex = 0;	// set card index to zero

				// read from file with the updated try statement (try with resources)
			try (FileReader fileReader = new FileReader(myFile);
					BufferedReader reader = new BufferedReader(fileReader)) {

				String line = null;
				while ((line = reader.readLine()) != null) {
						
						// check whether the user selected the right file worth reading
					if (!line.contains("QUESTION") || !line.contains("ANSWER")) {
						JOptionPane.showMessageDialog(frame,
								"File you are trying to load is not created from the 'Create card set' menu option!",
								"Error", JOptionPane.ERROR_MESSAGE);
						currentFileName.setText(null);
						return;
					}

						// proceed with making a card
					makeCard(line);

				}
				
					// add further proper information messages for the user
				JOptionPane.showMessageDialog(frame, "You have successfully loaded a file '" + myFile.getName()
						+ "' with the following number of questions: " + cardList.size() + "\nEnjoy the game!");
				
				txtArea.setText(null);

				btnShow.setEnabled(true);

				currentFileName.setText("Current file name: '" + myFile.getName() + "'");

			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(frame, ioe, "Error", JOptionPane.ERROR_MESSAGE);
			}

		}

		private boolean checkup() {

			int saveDialog = fileChooser.showSaveDialog(frame);

				// check if the user clicked cancel button
			if (saveDialog != JFileChooser.APPROVE_OPTION && currentFileName.getText().length() == 0) {
				JOptionPane.showMessageDialog(frame, "You must select a proper file with set questions and answers!",
						"Warning", JOptionPane.WARNING_MESSAGE);
				return false;
			}

				// check if there was a file name loaded and user wanted to load a new file, but
				// didn't select any new file
			if (saveDialog != JFileChooser.APPROVE_OPTION && currentFileName.getText().length() != 0) {
				return false;
			}

				// get the file finally
			myFile = fileChooser.getSelectedFile();

				// check if the file name really ends with '.txt' extension
			if (!myFile.getName().endsWith("txt")) {
				JOptionPane.showMessageDialog(frame,
						"You must select a file with the '.txt' extension that is explicitly set in the file name itself!",
						"Warning", JOptionPane.ERROR_MESSAGE);
				return false;
			}

				// check if the file is empty
			if (myFile.length() == 0) {
				JOptionPane.showMessageDialog(frame, "File you are trying to load is empty!", "Warning",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}

				// all checks successfully passed
			return true;

		}

		private void makeCard(String parseAtThisPosition) {
				// split each line on the character ";" to get the answer and it's question
			String token[] = parseAtThisPosition.split(";");
			Card card = new Card(token[0], token[1]);
			//System.out.println("MADE CARD: " + card.getQuestion() + " ; " + card.getAnswer());
			cardList.add(card);
		}

	}

	private class ShowActionListener implements ActionListener {	// display question/asnwer

		@Override
		public void actionPerformed(ActionEvent e) {

			if ( !checkup() )
				return;
			
				// display questions and answers one by one until the end of loaded file
			if (currentCardIndex != cardList.size()) {
				
					// simple logic to display question/answer at any given point
				if (!questionShown)
					showQuestion();
				else
					showAnswer();
				
			} else {
				txtArea.setText("No more questions to load! That's it!");
				btnShow.setEnabled(false);
			}
		}
		
        	// perform all the necessary checks and stop if something failed, otherwise continue with button's text changing
		private boolean checkup() {
			
				// check if there is a card loaded
			if (cardList.isEmpty() && myFile == null) {
				JOptionPane.showMessageDialog(frame,
						"You must first load a file(card set) with questions or create it!", "Error",
						JOptionPane.ERROR_MESSAGE);
				btnShow.setEnabled(false);
				return false;
			}
			
				// change button text appropriately
			else if (btnShow.getText().contains("Question"))
				btnShow.setText("Show Answer");
			else if (btnShow.getText().contains("Answer"))
				btnShow.setText("Show Question");

			return true;
		}

			// show question
		private void showQuestion() {
			txtArea.setText(cardList.get(currentCardIndex).getQuestion().trim());	// display question
			questionShown = true;	// tell the system that the question is currently displayed
		}

			// show answer
		private void showAnswer() {
			txtArea.setText(cardList.get(currentCardIndex).getAnswer().trim());	// display answer
			questionShown = false;	// tell the system that the question is currently not displayed
			currentCardIndex++;	// continue onto next question please
		}

	}
	
	private class ExitActionListener implements ActionListener {	// exit the playground

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);		
		}
		
	}

}

