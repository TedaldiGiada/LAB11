package it.unibo.oop.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


@SuppressWarnings("PMD.AvoidPrintStackTrace")
public final class ConcurrentGUI<Agent> extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private final JFrame frame = new JFrame();
    private final JLabel display = new JLabel();
    private final JButton stop = new JButton("stop");
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * 0.2), (int) (screenSize.getHeight() * 0.1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(display);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);
        
        final Agent agent = new Agent();
        executor.execute(agent);

        stop.addActionListener((e) -> {
            agent.stopCounting();
            executor.shutdownNow();
        });
        up.addActionListener((e) -> {agent.setDirection(true); });
        down.addActionListener((e) -> {agent.setDirection(false); });
    }
    private class Agent implements Runnable {
        
        private volatile boolean stop;
        private boolean dir = true;
        private int counter;

        @Override
        public void run() {
            while (!this.stop) {
                try {
                    final var nextText = Integer.toString(this.counter);
                    SwingUtilities.invokeLater(() -> ConcurrentGUI.this.display.setText(nextText));
                    if(dir) {
                        this.counter++;
                    } else {
                        this.counter --;
                    }
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        public void setDirection(boolean x) {
            this.dir = x;
        }

        public void stopCounting() {
            this.stop = true;
        }
    }
}


