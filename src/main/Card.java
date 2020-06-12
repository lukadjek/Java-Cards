package main;

/**
 * 
 * @author Luka
 *	Class name: Card
 *	Class job: this class is used to represent a simple card. Each card has one question and it's related answer.
 *	Two cards are same only if they have the same question.
 */

public class Card {

	private String question;
	private String answer;
	
	public Card(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	@Override
	public boolean equals(Object obj) {
		Card otherCard = (Card) obj;
		return otherCard.getQuestion().equals(question);
	}
	
	@Override
	public String toString() {
		return "QUESTION: " + question + " ; ANSWER: " + answer + "\n";
	}
	
}

