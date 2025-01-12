import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static ArrayList<String> targetWords = new ArrayList<>();
    public static String targetWord;
    public static String currentGuess;
    public static int remainingGuesses = 6;
    public static String incorrectGuesses = "";

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File("wordlist.txt"));
        while(in.hasNext()){
            targetWords.add(in.next());
        }
        in.close();
        targetWord = getWord();
        currentGuess = new String(new char[targetWord.length()]).replace("\0", "_");
        SwingUtilities.invokeLater(() -> {
            try {
                new HangmanGUI().setVisible(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public static String getWord(){
        Random r = new Random();
        String word = targetWords.get(r.nextInt(targetWords.size()));
        System.out.println("The target word is: " + word);
        return word;
    }
}

class HangmanGUI extends JFrame {
    private JTextField guessField;
    private JButton submitButton;
    private JLabel wordLabel, remainingGuessesLabel, incorrectGuessesLabel;

    public HangmanGUI() throws FileNotFoundException {
        setLayout(new FlowLayout());
        setTitle("Hangman");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        guessField = new JTextField(10);
        add(new JLabel("Guess"));
        add(guessField);

        submitButton = new JButton("Submit Guess");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String guess = guessField.getText().toLowerCase();
                String newGuess = "";
                for (int i = 0; i < Main.targetWord.length(); i++) {
                    if (Main.targetWord.charAt(i) == guess.charAt(0)) {
                        newGuess += guess.charAt(0);
                    } else if (Main.currentGuess.charAt(i) != '_') {
                        newGuess += Main.targetWord.charAt(i);
                    } else {
                        newGuess += "_";
                    }
                }

                if (Main.currentGuess.equals(newGuess)) {
                    Main.remainingGuesses--;
                    Main.incorrectGuesses += " " + guess;
                } else {
                    Main.currentGuess = newGuess;
                }

                if (Main.currentGuess.equals(Main.targetWord)) {
                    JOptionPane.showMessageDialog(null, "You win! You have guessed '" + Main.targetWord + "' correctly.");
                    System.exit(0);
                } else if (Main.remainingGuesses == 0) {
                    JOptionPane.showMessageDialog(null, "You have run out of guesses. The word was '" + Main.targetWord + "'.");
                    System.exit(0);
                }

                wordLabel.setText(Main.currentGuess);
                remainingGuessesLabel.setText("Guesses remaining: " + Main.remainingGuesses);
                incorrectGuessesLabel.setText("Incorrect guesses: " + Main.incorrectGuesses);
                guessField.setText("");
            }
        });
        add(submitButton);

        wordLabel = new JLabel(Main.currentGuess);
        add(wordLabel);

        remainingGuessesLabel = new JLabel("Guesses remaining: " + Main.remainingGuesses);
        add(remainingGuessesLabel);

        incorrectGuessesLabel = new JLabel("Incorrect guesses: " + Main.incorrectGuesses);
        add(incorrectGuessesLabel);
    }
}
