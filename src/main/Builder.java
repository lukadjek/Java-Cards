package main;

/**
 * 
 * @author Luka
 *	Class name: Builder
 *	Class job: this class is used to create cards and to save all created cards in the set.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Random;

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
import javax.swing.ScrollPaneConstants;


public class Builder extends javax.swing.JDialog {
	

	private JFrame frame;
	private ArrayList<Card> cardList;
	private JTextArea txtAreaQuestion;
	private JTextArea txtAreaAnswer;
	private JButton btnSaveThisCard;
	private JMenuItem saveMenuItem;
	private String question;
	private String answer;
	private JLabel numberOfCards;
	private boolean saved;
	

	public Builder() {
			// make everything here clear, concise and understandable with each method doing only one thing (good OOP) :D
		run();
	}

	void run() {
		
		setUp();
		addComponents();
		finalTouch();
		
	}


	private void setUp() {	// general set up 
		
		frame = new JFrame("Builder");
		frame.setSize(480, 550);
		frame.addWindowListener(new MyWindowAdapter());
				
	}
		   	

	private void addComponents() {	// components/visual stuff
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		saveMenuItem = new JMenuItem("Save all cards from current set");
		saveMenuItem.addActionListener(new SaveMenuListener());
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.addActionListener(new NewMenuListener());

		fileMenu.add(saveMenuItem);
		fileMenu.add(newMenuItem);
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);
		
        Font bigFont = new Font("sanserif", Font.BOLD, 15);

		
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.GRAY);
		frame.add(mainPanel, BorderLayout.CENTER);
		
		JLabel lblQuestion = new JLabel("Question");
		txtAreaQuestion = new JTextArea(10, 30);
		txtAreaQuestion.setFont(bigFont);
		txtAreaQuestion.setLineWrap(true);
		txtAreaQuestion.setWrapStyleWord(true);
		
		JLabel lblAnswer = new JLabel("Answer");
		txtAreaAnswer = new JTextArea(10, 30);
		txtAreaAnswer.setFont(bigFont);
		txtAreaAnswer.setLineWrap(true);
		txtAreaAnswer.setWrapStyleWord(true);
		
        JScrollPane questionScroller = new JScrollPane(txtAreaQuestion);
        questionScroller.setVerticalScrollBarPolicy(
                  ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        questionScroller.setHorizontalScrollBarPolicy(
                  ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);	
        
        
        JScrollPane answerScroller = new JScrollPane(txtAreaAnswer);
        answerScroller.setVerticalScrollBarPolicy(
                  ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        answerScroller.setHorizontalScrollBarPolicy(
                  ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
		txtAreaQuestion.addKeyListener(new MyKeyAdapter());
		txtAreaAnswer.addKeyListener(new MyKeyAdapter());
		
        btnSaveThisCard = new JButton("Save this card");
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        btnSaveThisCard.setCursor(handCursor);
        btnSaveThisCard.addActionListener(new NextCardListener());
        
        cardList = new ArrayList<Card>();
        numberOfCards = new JLabel();
        
        saved = false;
          
		mainPanel.add(lblQuestion);
		mainPanel.add(questionScroller);
		mainPanel.add(lblAnswer);
		mainPanel.add(answerScroller);
		mainPanel.add(btnSaveThisCard);
		mainPanel.add(numberOfCards);

	}

	private void clearQuizCard() {	// clear the current card's question and answer and add focus to the question field
		txtAreaQuestion.setText(null);
		txtAreaAnswer.setText(null);
		txtAreaQuestion.requestFocus();
	}
	
	private class MyWindowAdapter extends WindowAdapter {	// check if the user saved all cards in the set and regulate the closing of this form
		@Override
		public void windowClosing(WindowEvent e) {
			
			if ( !saved ) {
				String[] options = {"Close it", "Stay here"};
				int message = JOptionPane.showOptionDialog(frame,"No set of cards saved!\nYou should save a card set before closing this window!\nAre you sure you want to close it?","Error", 0,JOptionPane.WARNING_MESSAGE,null,options,null);
				
				if ( message == JOptionPane.YES_OPTION )
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				else
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			}
			else
	        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);			
		}
	}
	
	private class MyKeyAdapter extends KeyAdapter {	// make it easier for the user to add cards with focus and enter key 
		@Override
		public void keyPressed(KeyEvent e) {
			if ( e.getKeyCode() == KeyEvent.VK_ENTER ) {
				if ( txtAreaQuestion.getText().trim().length() > 0 )
					addFocusToAnswerArea();
				if ( txtAreaAnswer.getText().trim().length() > 0 ) {
					btnSaveThisCard.doClick();
					addFocusToQuestionArea();
				}
				e.consume();		// do not allow a new line to be entered (this can cause problems with saved cards document style later)
			}
		}
		
		public void addFocusToQuestionArea() {
			txtAreaQuestion.requestFocus();
		}
		
		public void addFocusToAnswerArea() {
			txtAreaAnswer.requestFocus();
		}

		public void addSuitableFocus() {	// add focus only to the first encountered empty field
			
			if ( txtAreaQuestion.getText().trim().length() == 0 )
				addFocusToQuestionArea();
			else if ( txtAreaAnswer.getText().trim().length() == 0 ) {
				addFocusToAnswerArea();
			}
			
		}
	}

	
	private class SaveMenuListener implements ActionListener {	// do the saving stuff

		@Override
		public void actionPerformed(ActionEvent e) {
			
			
			if ( cardList.isEmpty() ) {		// don't allow any saving if there's nothing to be saved
				JOptionPane.showMessageDialog(frame, "No cards in the set to be saved!", "Error", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
				// make the default location be the user's desktop
			String openLocation = System.getProperty("user.home") + "/Desktop";
						
			JFileChooser fileSave = new JFileChooser(openLocation);

			fileSave.showSaveDialog(frame);

			File fileLocation = fileSave.getSelectedFile();
			
			if ( fileLocation != null ) {
			
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(fileLocation));
					// write each question and answer in one line together with it's order number
				int num = 1;
				for (Card element : cardList) {
					writer.write(num + "." + element.toString());
					num++;
				}
				
				writer.close();
				JOptionPane.showMessageDialog(frame, num-1 + " cards saved in the file named: " + fileLocation.getName());
				saved = true;
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(frame, ioe, "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
			else
				JOptionPane.showMessageDialog(frame, "File not saved!", "Error", JOptionPane.WARNING_MESSAGE);
		}
		
	}
	
	private class NextCardListener implements ActionListener {	// open next card

		@Override
		public void actionPerformed(ActionEvent e) {
			
				// why not play with some colors to make the learning process easier, engaging and more fun
			Random random = new Random();
			float hue = random.nextFloat() * 100;
			final float sat = 0.2f;
			final float lum = 0.8f;
			Color randomColor = Color.getHSBColor(hue, sat, lum);
			
	        question = txtAreaQuestion.getText().trim();	// trim question JTextArea to make sure there are no empty lines added to the saved file
	        answer = txtAreaAnswer.getText().trim();		// trim answer JTextArea to make sure there are no empty lines added to the saved file

	        
	        	// if the question and answer are all good to go continue with saving that card
			if ( question.trim().length() != 0 && answer.trim().length() != 0 ) {
			
				Card quizCard = new Card(question, answer);
			
				if ( !cardList.contains(quizCard) ) {	// don't allow having the same question in the set
					cardList.add(quizCard);
					txtAreaQuestion.setBackground(randomColor);
					txtAreaAnswer.setBackground(randomColor);
				
					System.out.println("*We have just added a new quiz card*\n" + quizCard);
					
					clearQuizCard();
					
					numberOfCards.setText("Number of cards in the set: " + cardList.size());	// let the user always know how many cards are there 
				}
				else {
					JOptionPane.showMessageDialog(frame, "There is already a card with this question!\n Card not saved!", "Error", JOptionPane.WARNING_MESSAGE);
					new MyKeyAdapter().addFocusToQuestionArea();
				}

							
			}
			else {
				JOptionPane.showMessageDialog(frame, "Both fields must contain some text!", "Error", JOptionPane.ERROR_MESSAGE);
				new MyKeyAdapter().addSuitableFocus();
			}
			
		}
		
	}
	
	private class NewMenuListener implements ActionListener {	// if the user made any mistake simply clear the whole card list

		@Override
		public void actionPerformed(ActionEvent e) {

			if (cardList.size() > 0) {

				clearQuizCard();
				cardList.clear();

				txtAreaQuestion.setBackground(Color.white);
				txtAreaAnswer.setBackground(Color.white);
				numberOfCards.setText(null);

				JOptionPane.showMessageDialog(frame, "Card list is now cleared.");
			}
			else
				JOptionPane.showMessageDialog(frame, "Nothing yet to clear!", "Error", JOptionPane.WARNING_MESSAGE);
		}
		
	}
	
	
	private void finalTouch() {	// center the frame on the screen
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}

}

