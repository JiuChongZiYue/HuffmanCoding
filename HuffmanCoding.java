package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */


public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */

    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);
	    
        ArrayList<Character> chars = new ArrayList<>();
        ArrayList<Integer> nums = new ArrayList<>();

        while (StdIn.hasNextChar() == true){
            char c = StdIn.readChar();
            if(! chars.contains(c)){
                chars.add(c);
                nums.add(1);
            }else if (chars.contains(c)){
                int b = chars.indexOf(c);  //the index of exist character
                int d = nums.get(b) + 1;       //the value of that character add 1
                nums.set(b, d);
            }
        }

        // System.out.println(chars);
        // System.out.println(nums);

        int max = 0;
        
        for (int i = 0; i < nums.size(); i++){
            int n = nums.get(i);
            max = max + n;
        } 

        // Collections.sort(list);
        
        // char tempc;
        // int tempn;
        // for (int i = 0; i < chars.size(); i++){
        //     for(int j = i; j < chars.size(); j++){
        //         if (nums.get(i) > nums.get(j) ){
        //             tempc = chars.get(i);
        //             chars.set(i, chars.get(j));
        //             chars.set(j, tempc);

        //             tempn = nums.get(i);
        //             nums.set(i, nums.get(j));
        //             nums.set(j, tempn);
        //         }
        //     }
        // }

        //System.out.println(" unsorted " + chars + " " + nums);
        

        //ArrayList<CharFreq> sortedCharFreqList = new ArrayList<>();
        ArrayList<CharFreq> list = new ArrayList<>();
 
        for (int i = 0; i < chars.size(); i++){
            //char c = chars.get(i).charAt(0);    //return the char in the string wich the index is 0
            double d = ( (double)nums.get(i) ) / ( (double)max ); //the value of double for each letters
            //System.out.println( "the d for charf " + d);
            char c = chars.get(i);                //the char at index c

            CharFreq x = new CharFreq(c, d); //new node for the charfreq arraylist

            list.add(x);       //add the new node in arraylist
        }
        
        if( list.size() == 1){
            char c = list.get(0).getCharacter();
            int n = (int)c;
            

            if (c == 127){
                c = (char) n;
            }else{
                n +=1;
                c = (char) n;
            }
            CharFreq x = new CharFreq(c, 0);
            list.add(x);

        }

        Collections.sort(list);
        
        sortedCharFreqList = list;

        // System.out.println(" charFreq " + sortedCharFreqList.size());


    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {
	    /* Your code goes here */
        Queue<CharFreq> source = new Queue<>();
        Queue<TreeNode> target = new Queue<>();

        // System.out.println(sortedCharFreqList.size());

        for(int i = 0; i < sortedCharFreqList.size(); i++){  //enqueue all charF in the source
            source.enqueue(sortedCharFreqList.get(i));
        }

        while( !source.isEmpty()  ||  target.size() != 1){
            if (target.size() == 0){    //cccccccccc if there is no data in target, nothing can be dequeue in target
                TreeNode x = new TreeNode();   //for the first dequeue CharF
                TreeNode y = new TreeNode();   //for the second dequeue charF
                TreeNode z = new TreeNode();   //for the charF that have letter null and probility of x and y
                

                x.setData(  source.dequeue()  );
                y.setData(  source.dequeue()  );
                double first = x.getData().getProbOcc();    //the probability of x
                double second = y.getData().getProbOcc();   //the probability of y
                CharFreq f = new CharFreq(null, first + second); //make new CharF node that have the sum
                z.setData(f);   //z will have a the null-proSum node
                z.setLeft(x);   //z goes left to x(smaller)
                z.setRight(y);  //z goes right to y(bigger)
                target.enqueue(z);
            }else if(target.size() != 1 && source.isEmpty() ){  //ccccccccc
                TreeNode x = new TreeNode();
                TreeNode y = new TreeNode();
                TreeNode z = new TreeNode();

                //x.setData(  target.dequeue().getData()  );
                x=target.dequeue();
                //y.setData(  target.dequeue().getData()  );
                y=target.dequeue();


                double first = x.getData().getProbOcc();    //the probability of x
                double second = y.getData().getProbOcc();   //the probability of y
                CharFreq f = new CharFreq(null, first + second); //make new CharF node that have the sum
                z.setData(f);   //z will have a the null-proSum node
                z.setLeft(x);   //z goes left to x(smaller)
                z.setRight(y);  //z goes right to y(bigger)
                target.enqueue(z);                
            }
            
            
            else if (source.peek().getProbOcc() <= target.peek().getData().getProbOcc()  ){ //cccccc F1 first source is smaller
                TreeNode x = new TreeNode();
                x.setData(source.dequeue());

                if (source.isEmpty() ){ //bbcccccccc if source is empty, then dequeue target
                    TreeNode y = new TreeNode(); //right big
                    TreeNode z = new TreeNode(); //null 
                    
                    y = target.dequeue();

                    double first = x.getData().getProbOcc();
                    double second = y.getData().getProbOcc();

                    CharFreq f = new CharFreq(null, first + second);
                    z.setData(f);   //z will have a the null-proSum node
                    z.setLeft(x);   //z goes left to x(smaller)
                    z.setRight(y);  
                    target.enqueue(z);
                }
                else if(source.peek().getProbOcc() <= target.peek().getData().getProbOcc() ){   //F2 second source is smaller
                    TreeNode y = new TreeNode();
                    TreeNode z = new TreeNode();
                    y.setData(source.dequeue());

                    double first = x.getData().getProbOcc();
                    double second = y.getData().getProbOcc();
                    
                    CharFreq f = new CharFreq(null, first + second);
                    z.setData(f);   //z will have a the null-proSum node
                    z.setLeft(x);   //z goes left to x(smaller)
                    z.setRight(y);  
                    target.enqueue(z);
                

                //problem has when inter this , can not find d c
                }else if (source.peek().getProbOcc() > target.peek().getData().getProbOcc() ){  //F2 first target is smaller
                    TreeNode y = new TreeNode();
                    TreeNode z = new TreeNode();
                    
                    y = target.dequeue();

                    double first = x.getData().getProbOcc();
                    double second = y.getData().getProbOcc();

                    CharFreq f = new CharFreq(null, first + second);
                    z.setData(f);   //z will have a the null-proSum node
                    z.setLeft(x);   //z goes left to x(smaller)
                    z.setRight(y);
                    target.enqueue(z);
                }
            }
            
            
            else if(source.peek().getProbOcc() > target.peek().getData().getProbOcc()  ){  //F1 first target is smaller
                TreeNode x = new TreeNode();

                //x.setData(target.dequeue().getData());
                x = target.dequeue();
                //double checking = x.getData().getProbOcc();    //if there is only one data in the target, then after the first dequeue, the second dequeeu will only be in the source queue

                if (target.isEmpty() == true){
                    TreeNode y = new TreeNode(); //right big
                    TreeNode z = new TreeNode(); //null 
                    y.setData(source.dequeue()); //dequeue again

                    double first = x.getData().getProbOcc();
                    double second = y.getData().getProbOcc();

                    CharFreq f = new CharFreq(null, first + second);
                    z.setData(f);   //z will have a the null-proSum node
                    z.setLeft(x);   //z goes left to x(smaller)
                    z.setRight(y);  
                    target.enqueue(z);
                }
                else if(source.peek().getProbOcc() <= target.peek().getData().getProbOcc() ){   //F2 first source is smaller
                    TreeNode y = new TreeNode();
                    TreeNode z = new TreeNode();
                    y.setData(source.dequeue());

                    double first = x.getData().getProbOcc();
                    double second = y.getData().getProbOcc();
                    
                    CharFreq f = new CharFreq(null, first + second);
                    z.setData(f);   //z will have a the null-proSum node
                    z.setLeft(x);   //z goes left to x(smaller)
                    z.setRight(y);  
                    target.enqueue(z);
                }else if (source.peek().getProbOcc() > target.peek().getData().getProbOcc() ){  //F2 second target is smaller
                    TreeNode y = new TreeNode();
                    TreeNode z = new TreeNode();
                    //y.setData(target.dequeue().getData());
                    y = target.dequeue();

                    double first = x.getData().getProbOcc();
                    double second = y.getData().getProbOcc();

                    CharFreq f = new CharFreq(null, first + second);
                    z.setData(f);   //z will have a the null-proSum node
                    z.setLeft(x);   //z goes left to x(smaller)
                    z.setRight(y);
                    target.enqueue(z);
                }
            }
        }
        huffmanRoot = target.dequeue();
    }

    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    private static void LR (String[] str, TreeNode n, String code){
        if (n.getLeft() != null && n.getRight() != null){
            
            LR(str, n.getLeft(), code + '0');
            LR(str, n.getRight(), code + '1');
        }
        else if (n.getLeft() == null && n.getRight() == null){
            char c = n.getData().getCharacter();
            str[(int) c] = code;
        }
    }

    public void makeEncodings() {
    	/* Your code goes here */
        String[] haffmanCode = new String[128];
        String code = "";

        LR(haffmanCode, huffmanRoot, code);
        encodings = haffmanCode;
        //System.out.println(encodings.toString());
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);

	    /* Your code goes here */
        String newtext = new String();
        newtext = "";

        while(StdIn.hasNextChar()){
            char r = StdIn.readChar();
            int c = (int) r;
            newtext = newtext + encodings[c];
        }

        writeBitString(encodedFile, newtext);

    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);
	    /* Your code goes here */
        TreeNode x = new TreeNode();
        x = huffmanRoot;
        char s;
        StdIn.setFile(encodedFile);
        String str =  readBitString(encodedFile);
        String f = "";

        int i = 0;
        int h = 0;
        while ( i < str.length() ){
            char c = str.charAt(i); //it will be zero or one
            if (0 == Character.getNumericValue(c)){
                x = x.getLeft();
            }else if (1 == Character.getNumericValue(c)) {
                x = x.getRight();
            }
            if ( x.getLeft() ==null && x.getRight() == null){
                s = x.getData().getCharacter();

                StdOut.print(s);
                f = f + s;

                x = huffmanRoot;
            }
            i++;
        }


    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}
