SPECIFICATION

Short description: This software is useful for anyone who is willing to test their knowledge in an interesting, fun and proactive way. It can be used for students to quickly check up on their subject before any oral exam by getting a question, answering by-heart, getting next question and so on until there's no more questions! Of course, all questions and their appropriate answers are created by the user.

Important note: whole code is written manually ("design" tab from Eclipse was not used).

*** Available options for "create new card set" menu option:
1. add one question, add one answer, save it as one card, continue
2. each saved card from the set is represented in one line of the file (each line contains one question and it's answer)
3. checks and help added: 
    * a) question area cannot be empty
    * b) answer area cannot be empty
    * c) it is not allowed to have two same questions in the set (there cannot be two same cards)
    * d) keyboard key "enter" can be used to quickly jump in this order: question -> answer -> "save this card" button
    * e) focus will always be there where you'll need it. In other words, focus is requested:
        * e.1) to the question area if "save this card" button is pressed and question area is empty
        * e.2) to the question area if "save this card" button is pressed and the question already exists
        * e.3) to the answer area if "save this card" button is pressed and answer area is empty
    * f) each press on the "save this card" button creates a new empty card with a random color (provided there are no                errors)
    * g) appropriate information message is shown to the user so that he/she knows exactly the current status of the                  software (this includes all error messages, warning messages and success messages)
    * h) when exiting this gui, the software checks whether the user saved all cards in the set or not
    * i) in case the user saved a card with a wrong question/answer, he/she can use the "new" menu option to delete all              cards and start over
    * j) shortcut to open this menu option: CTRL + 8
    
*** Available options for "load card set" menu option:
1. by clicking on this option, a save dialog opens up (default location is users's desktop)
2. added checks:
    * a) loaded file must not be empty
    * b) loaded file must be of ".txt" extension
    * c) loaded file must be created from the "create new card set" menu option (it's document style must look like this:
         $NUM.$QUESTION: $QUESTION_TEXT$ ; ANSWER: $ANSWER_TEXT$ ); ($NUM.$ represents the question number)
    * d) ".txt" extension must be set explicitly in the file name itself
3. after successful file load/selection, an appropriate information message is displayed and the game can finally begin
4. game process: 
     * a) user clicks on the "show question" button
     * b) first question is shown
     * c) button text is changed from "show question" to "show answer"
     * d) user answers/speaks the question quietly/loudly in front of his computer
     * e) user clicks on the "show answer" button
     * f) answer is shown on the screen
     * g) button text is changed from "show answer" to "show question"
     * h) user is content as he got it right/user is disappointed because he missed it
     * i) the whole process is repeated from a) to h) until there is no more questions in the loaded file
     * j) shortcut to open this menu option: CTRL + 9
5. name of the current loaded file is displayed next to the "show question"/"show answer" button

*** Available options for "exit" menu option:
1. by clicking on this menu option, the whole program ends and all open windows are closed.
2. shortcut to open this menu option: CTRL + 0


