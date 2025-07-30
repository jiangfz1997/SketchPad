package sketchpad.util;

import sketchpad.shape.Shape;

import java.io.*;
import java.util.List;

public class FileIO {

    public static void saveToFile(List<Shape> shapes, File file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(shapes);
        }
    }

    public static List<Shape> loadFromFile(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Shape>) in.readObject();
        }
    }
}
