import java.awt.*
import java.awt.geom.RoundRectangle2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URL
import javax.imageio.ImageIO
import kotlin.collections.List

data class ImageProperties(val width: Int, val height: Int, val backgroundImagePath: String, val imageTextElements: List<ImageTextElement?>, val imageImageElements: List<ImageImageElement>)
data class ImageTextElement(val x: Int, val y: Int, val font: Font, val color: Color, val text: String, val maxWidth: Int)
data class ImageImageElement(val x: Int, val y: Int, val color: Color, val borderWidth: Int, val imageWidth: Int, val imageHeight: Int, val resourceUrl: String?, val resourceFilePath: String?)

fun imageCreator(imageProperties: ImageProperties): ByteArray
{
    // ImageSettings
    val panelwidth = imageProperties.width
    val panelheight = imageProperties.height
    val backgroundFile = File(imageProperties.backgroundImagePath)

    // Initializing BackgroundImageFile
    val backgroundImage = ImageIO.read(backgroundFile)

    // Creating BufferedImage
    val image = BufferedImage(panelwidth, panelheight, BufferedImage.TYPE_INT_ARGB)

    // Creating GraphicsInterface
    val g2d = image.createGraphics()

    // Add Background onto the Image
    g2d.drawImage(backgroundImage, 0, 0, panelwidth, panelheight, null)

    // Drawing ImageTextElements
    if (imageProperties.imageTextElements.isNotEmpty())
    {
        for (imageTextElement in imageProperties.imageTextElements)
        {
            var fontMetrics = g2d.getFontMetrics(imageTextElement?.font)
            val textWidth = imageTextElement!!.text.let { fontMetrics.stringWidth(it) }
            g2d.font = imageTextElement.font
            if (textWidth > imageTextElement.maxWidth)
            {
                val scaleFactor = imageTextElement.maxWidth / textWidth
                val newSize = (g2d.font.size2D * scaleFactor)
                g2d.font = g2d.font.deriveFont(newSize)
                fontMetrics = g2d.getFontMetrics(g2d.font)
                imageTextElement.text.let { fontMetrics.stringWidth(it) }
            }
            g2d.color = imageTextElement.color
            imageTextElement.text.let { g2d.drawString(it, imageTextElement.x, imageTextElement.y) }
        }
    }

    // Drawing ImageImageElements
    var errorImage: Int = 0
    for (imageImageElement in imageProperties.imageImageElements)
    {
        errorImage++
        if (imageImageElement.resourceUrl != null)
        {
            val imageUrl = URL(imageImageElement.resourceUrl)
            val bufferedImage = ImageIO.read(imageUrl)
            val roundedImage = createRoundedImage(bufferedImage, imageImageElement.imageWidth, imageImageElement.imageHeight, imageImageElement.borderWidth, imageImageElement.color)

            g2d.drawImage(roundedImage, imageImageElement.x, imageImageElement.y, null)
        }
        else if (imageImageElement.resourceFilePath != null)
        {
            val imageFile = File(imageImageElement.resourceFilePath)
            val bufferedImage = ImageIO.read(imageFile)
            val roundedImage = createRoundedImage(bufferedImage, imageImageElement.imageWidth, imageImageElement.imageHeight, imageImageElement.borderWidth, imageImageElement.color)

            g2d.drawImage(roundedImage, imageImageElement.x, imageImageElement.y, null)
        }
        else
        {
            println("Missing ImageResource for the image #$errorImage")
        }
    }

    // Convert the image to a byte array for use as an attachment in the Discord Embed message
    val baos = ByteArrayOutputStream()
    ImageIO.write(image, "png", baos)

    return baos.toByteArray()
}
/* archived code
fun imageAboutCreator(
    avatarUrlString: String,
    title: String,
    descriptionLine1: String,
    backgroundFilePath: String): ByteArray
{
    val panelwidth = 1300
    val panelheight = 300

    // Load the background image from the file
    val backgroundFile = File(backgroundFilePath)
    val backgroundImage = ImageIO.read(backgroundFile)

    // Create a new buffered image using the panel width and height
    val image = BufferedImage(panelwidth, panelheight, BufferedImage.TYPE_INT_ARGB)

    // Get the graphics context for the buffered image
    val g2d = image.createGraphics()

    // Draw the background image to the graphics context
    g2d.drawImage(backgroundImage, 0, 0, panelwidth, panelheight, null)

    // Set the background color of the panel
    // g2d.color = Color.BLACK
    // g2d.fillRect(0, 0, panelwidth, panelheight)

    // Load the user's avatar image and create a rounded version of it
    val avatarImage = prepareImageFromUrl(avatarUrlString)
    val roundedAvatarImage = createRoundedImage(avatarImage, 250, 250, 5, Color.WHITE)

    // Draw the rounded avatar image to the graphics context
    g2d.drawImage(roundedAvatarImage, 25, 25, null)

    // Create a new font object for the title label
    var font = Font("Arial", Font.BOLD, 225)
    g2d.font = font

    // Draw the title label
    g2d.color = Color.WHITE
    g2d.drawString(title, 300, 200)

    // Change the font object for the description label
    font = Font("Arial", Font.BOLD, 45)
    g2d.font = font
    g2d.drawString(descriptionLine1, 320, 275)

    /* archived code
    // Change the font object for the description label
    font = Font("Arial", Font.PLAIN, 25)
    g2d.font = font
    g2d.drawString(descriptionLine1, 300/*panelwidth / 2 - fontMetrics.stringWidth(description) / 2*/, 110)
    g2d.drawString(descriptionLine2, 300/*panelwidth / 2 - fontMetrics.stringWidth(description) / 2*/, 140)
    g2d.drawString(descriptionLine3, 300/*panelwidth / 2 - fontMetrics.stringWidth(description) / 2*/, 170)
     */

    // Clean up the graphics context
    g2d.dispose()

    // Convert the image to a byte array for use as an attachment in the Discord Embed message
    val baos = ByteArrayOutputStream()
    ImageIO.write(image, "png", baos)

    return baos.toByteArray()
}
fun prepareImageFromUrl(imageUrlString: String): BufferedImage {
    // Load the user's avatar image from the URL
    val imageUrl = URL(imageUrlString)
    return ImageIO.read(imageUrl)
}
 */
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
