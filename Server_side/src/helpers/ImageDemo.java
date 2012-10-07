package helpers;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * A Java class to demonstrate how to load an image from disk with the
 * ImageIO class. Also shows how to display the image by creating an
 * ImageIcon, placing that icon an a JLabel, and placing that label on
 * a JFrame.
 * 
 * @author alvin alexander, devdaily.com
 */
public class ImageDemo extends Thread
{
	private final URL url;

	public ImageDemo(final URL url) throws Exception
	{
		this.url = url;
	}

	@Override
	public void run() {
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JFrame editorFrame = new JFrame("Image Demo");
				editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

				BufferedImage image = null;
				try
				{
					image = ImageIO.read(url);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.exit(1);
				}
				ImageIcon imageIcon = new ImageIcon(image);
				JLabel jLabel = new JLabel();
				jLabel.setIcon(imageIcon);
				editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

				editorFrame.pack();
				editorFrame.setLocationRelativeTo(null);
				editorFrame.setVisible(true);
			}
		});
	}
}