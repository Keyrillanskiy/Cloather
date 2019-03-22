package presentation.screens.settings

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import domain.models.entities.User
import domain.models.values.Gender
import kotlinx.android.synthetic.main.fragment_settings.view.*
import presentation.common.BaseViewHolder

/**
 * @author Keyrillanskiy
 * @since 22.02.2019, 12:37.
 */
class SettingsViewHolder(private val rootView: View) : BaseViewHolder(rootView) {

    var onLogOutClick: (() -> Unit)? = null
    var onGenderChanged: ((Gender) -> Unit)? = null
    var onNotificationsClick: ((isEnabled: Boolean) -> Unit)? = null

    fun setup(block: SettingsViewHolder.() -> Unit): SettingsViewHolder {
        block()

        rootView.run {
            settingsLogOutButton.setOnClickListener { onLogOutClick?.invoke() }
            settingsMaleRadioButton.setOnClickListener { onMaleClick() }
            settingsFemaleRadioButton.setOnClickListener { onFemaleClick() }
            settingsNotificationsContainer.setOnClickListener { onNotificationsClick?.invoke(false) } //TODO: change on switch
        }

        return this
    }

    var user: User? = null
        set(value) {
            field = value
            name = value?.name
            googleEmail = value?.googleEmail
            gender = value?.gender
            loadAvatar(value?.googleAvatarURL)
        }

    var name: String? = null
        set(value) {
            field = value
            rootView.settingsNameTextView.text = value
        }

    var googleEmail: String? = null
        set(value) {
            field = value
            rootView.settingsEmailTextView.text = value
        }

    var gender: Gender? = null
        set(value) {
            field = value
            when (value) {
                Gender.MALE -> {
                    rootView.run {
                        settingsMaleRadioButton.isChecked = true
                    }
                }
                Gender.FEMALE -> {
                    rootView.run {
                        settingsFemaleRadioButton.isChecked = true
                    }
                }
                else -> throw IllegalArgumentException("invalid gender: $value")
            }
        }

    private fun loadAvatar(avatarUrl: String?) {
        Glide.with(context)
            .load(avatarUrl)
            .apply(RequestOptions.centerCropTransform())
            .into(rootView.settingsAvatarImageView)
    }

    private fun onMaleClick() {
        val newGender = Gender.MALE
        onGenderChanged?.invoke(newGender)
    }

    private fun onFemaleClick() {
        val newGender = Gender.FEMALE
        onGenderChanged?.invoke(newGender)
    }

}