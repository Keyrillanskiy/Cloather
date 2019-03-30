package extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import utils.serverBaseUrl

/**
 * @author Keyrillanskiy
 * @since 06.03.2019, 12:43.
 */

fun loadImageFromBackend(context: Context, imageUrl: String?, imageView: ImageView) {
    val url = serverBaseUrl + imageUrl
    Glide.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(imageView)
}