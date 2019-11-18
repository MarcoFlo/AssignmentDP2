package it.polito.dp2.BIB;



public class Maintest {
    public static void main(String[] args) {
        try {
            System.setProperty("it.polito.dp2.BIB.BibReaderFactory", "it.polito.dp2.BIB.sol1.BibReaderFactory");
            BibReader bibReader = BibReaderFactory.newInstance().newBibReader();


        } catch (BibReaderException e) {
            System.out.println("Bib reader excpetion");
            e.printStackTrace();
        }

    }

}