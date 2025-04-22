import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.*;

public class ImageProcessor {
    private static volatile boolean stopFlag = false;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java ImageProcessor <directory> [/sub] <operation> [parameters]");
            return;
        }

        String rootPath = args[0];
        boolean recursive = args.length > 2 && "/sub".equalsIgnoreCase(args[1]);
        String operation = args[recursive ? 2 : 1];
        String param = args.length > (recursive ? 3 : 2) ? args[recursive ? 3 : 2] : null;

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Thread escListener = new Thread(() -> {
            try {
                System.in.read();
                stopFlag = true;
                executor.shutdownNow();
                System.out.println("Operation cancelled by user.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        escListener.setDaemon(true);
        escListener.start();

        try {
            Files.walk(Paths.get(rootPath), recursive ? Integer.MAX_VALUE : 1)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().matches(".*\\.(jpg|png|bmp|gif)$"))
                    .forEach(file -> executor.submit(() -> processImage(file.toFile(), operation, param)));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    private static void processImage(File file, String operation, String param) {
        if (stopFlag) return;

        try {
            switch (operation) {
                case "/s": // Resize image
                    if (param == null) return;
                    double scale = Double.parseDouble(param);
                    resizeImage(file, scale);
                    break;
                case "/n": // Negative image
                    invertColors(file);
                    break;
                case "/r": // Remove image
                    file.delete();
                    System.out.println("Deleted: " + file.getAbsolutePath());
                    break;
                case "/c": // Copy image
                    if (param == null) return;
                    copyImage(file, new File(param));
                    break;
                default:
                    System.out.println("Unknown operation: " + operation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void resizeImage(File file, double scale) throws IOException {
        BufferedImage img = ImageIO.read(file);
        int newWidth = (int) (img.getWidth() * scale);
        int newHeight = (int) (img.getHeight() * scale);

        BufferedImage resized = new BufferedImage(newWidth, newHeight, img.getType());
        Graphics2D g = resized.createGraphics();
        g.drawImage(img, 0, 0, newWidth, newHeight, null);
        g.dispose();

        ImageIO.write(resized, "jpg", file);
        System.out.println("Resized: " + file.getAbsolutePath());
    }

    private static void invertColors(File file) throws IOException {
        System.out.println("Processing: " + file.getAbsolutePath());
        BufferedImage img = ImageIO.read(file);

        // Инверсия цветов с сохранением альфа-канала
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int rgba = img.getRGB(x, y);
                int alpha = (rgba >> 24) & 0xFF; // Сохраняем прозрачность
                int r = 255 - ((rgba >> 16) & 0xFF);
                int g = 255 - ((rgba >> 8) & 0xFF);
                int b = 255 - (rgba & 0xFF);
                img.setRGB(x, y, (alpha << 24) | (r << 16) | (g << 8) | b);
            }
        }

        // Определяем формат по расширению файла
        String fileName = file.getName().toLowerCase();
        String format = fileName.endsWith(".png") ? "png" : "jpg";

        ImageIO.write(img, format, file);
        System.out.println("Negativized: " + file.getAbsolutePath());
    }

    private static void copyImage(File file, File destDir) throws IOException {
        if (!destDir.exists()) destDir.mkdirs();
        File destFile = new File(destDir, file.getName());
        Files.copy(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Copied: " + file.getAbsolutePath() + " -> " + destFile.getAbsolutePath());
    }
}