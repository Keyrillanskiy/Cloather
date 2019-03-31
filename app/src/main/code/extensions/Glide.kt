package extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import utils.serverBaseUrl

/**
 * @author Keyrillanskiy
 * @since 06.03.2019, 12:43.
 */

fun loadImageFromBackend(context: Context, imageUrl: String?, imageView: ImageView) {
    val url = serverBaseUrl + imageUrl
    Glide.with(context)
        .load(url)
        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(imageView)
}