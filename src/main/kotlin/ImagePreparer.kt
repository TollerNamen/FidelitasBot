import java.awt.*
import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.net.URL
import javax.imageio.ImageIO

fun imagePreparer(avatarUrlString: String, title: String, description: String): ByteArray {
    val panelwidth = 900
    val panelheight = 300

    // Create a new buffered image using the panel width and height
    val image = BufferedImage(panelwidth, panelheight, BufferedImage.TYPE_INT_ARGB)

    // Get the graphics context for the buffered image
    val g2d = image.createGraphics()

    // Set the background color of the panel
    g2d.color = Color.BLACK
    g2d.fillRect(0, 0, panelwidth, panelheight)

    // Load the user's avatar image and create a rounded version of it
    val avatarImage = prepareAvatar(avatarUrlString)
    val roundedAvatarImage = createRoundedImage(avatarImage, 300, 300, 5, Color.WHITE)

    // Draw the rounded avatar image to the graphics context
    g2d.drawImage(roundedAvatarImage, 0, 0, null)

    // archived code
    //g2d.drawImage(avatarImage, 0, 0, 50, 50, null)

    // Create a new font object for the title and description labels
    val font = Font("Arial", Font.PLAIN, 18)
    g2d.font = font

    // Get the FontMetrics object for the current font
    val fontMetrics = g2d.getFontMetrics(font)

    // Draw the title and description labels
    g2d.color = Color.WHITE
    g2d.drawString(title, panelwidth / 2 - fontMetrics.stringWidth(title) / 2, 30)
    g2d.drawString(description, panelwidth / 2 - fontMetrics.stringWidth(description) / 2, 50)

    // Clean up the graphics context
    g2d.dispose()

    // Convert the image to a byte array for use as an attachment in the Discord Embed message
    val baos = ByteArrayOutputStream()
    ImageIO.write(image, "png", baos)
    g2d.dispose()

    return baos.toByteArray()
}
fun prepareAvatar(avatarUrlString: String): BufferedImage {
    // Load the user's avatar image from the URL
    val avatarUrl = URL(avatarUrlString)
    return ImageIO.read(avatarUrl)
}
fun createRoundedImage(image: BufferedImage, width: Int, height: Int, borderWidth: Int, borderColor: Color): BufferedImage {
    // create a new image with transparency
    val outputImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    // get the graphics context for the output image
    val g2d = outputImage.createGraphics()

    // create a clipping shape
    val shape = RoundRectangle2D.Float(0f, 0f, width.toFloat(), height.toFloat(), width.toFloat(), height.toFloat())

    // set the clipping shape
    g2d.clip(shape)

    // draw the image with the clipping applied
    g2d.drawImage(image, 0, 0, width, height, null)

    // set the stroke and color for the border
    g2d.stroke = BasicStroke(borderWidth.toFloat())
    g2d.color = borderColor

    // draw a rounded rectangle with the stroke applied
    g2d.drawRoundRect(borderWidth/2, borderWidth/2, width - borderWidth, height - borderWidth, width, height)

    // dispose of the graphics context
    g2d.dispose()

    return outputImage
}