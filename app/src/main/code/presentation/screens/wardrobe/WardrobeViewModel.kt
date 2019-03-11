package presentation.screens.wardrobe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.preferences.Preferences
import data.useCases.interfaces.WardrobeUseCase
import domain.models.responses.Category
import domain.models.responses.Thing
import domain.models.values.Gender
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import presentation.common.Response
import presentation.common.failure
import presentation.common.loading
import presentation.common.success
import timber.log.Timber
import utils.SchedulersFacade

/**
 * @author Keyrillanskiy
 * @since 01.03.2019, 11:06.
 */
class WardrobeViewModel(
    private val wardrobeUseCase: WardrobeUseCase,
    private val preferences: Preferences,
    private val schedulers: SchedulersFacade
) : ViewModel() {

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    private val _categoriesLiveData = MutableLiveData<Response<List<Category>>>()
    val categoriesLiveData: LiveData<Response<List<Category>>>
        get() = _categoriesLiveData

    private val _thingsLiveData = MutableLiveData<Response<Pair<List<Thing>, List<Thing>>>>()
    val thingsLiveData: LiveData<Response<Pair<List<Thing>, List<Thing>>>>
        get() = _thingsLiveData

    private val _thingUpdateLiveData = MutableLiveData<Response<Unit>>()
    val thingUpdateLiveData: LiveData<Response<Unit>>
        get() = _thingUpdateLiveData

    val gender: Gender
        get() = preferences.gender

    fun fetchCategories(gender: Gender = preferences.gender) {
        wardrobeUseCase.fetchCaterories(gender)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
            .doOnSubscribe { _categoriesLiveData.value = Response.loading() }
            .subscribe(
                { _categoriesLiveData.value = Response.success(it) },
                {
                    _categoriesLiveData.value = Response.failure(it)
                    Timber.w(it)
                }
            )
            .addTo(disposables)
    }

    fun fetchThings(categoryId: String, userId: String? = preferences.uid) {
        Single.zip(
            wardrobeUseCase.fetchUsersWardrobe(userId),
            wardrobeUseCase.fetchThingsForWardrobeScreen(categoryId, preferences.gender),
            BiFunction { wardrobe: List<Thing>, thingsByCategory: List<Thing> -> wardrobe to thingsByCategory }
        )
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
            .doOnSubscribe { _thingsLiveData.value = Response.loading() }
            .subscribe(
                { _thingsLiveData.value = Response.success(it) },
                {
                    _thingsLiveData.value = Response.failure(it)
                    Timber.w(it)
                }
            )
            .addTo(disposables)
    }

    fun updateThing(thingItem: ThingItem, userId: String? = preferences.uid) {
        if (userId == null) {
            return
        }

        val completable = if (thingItem.isChecked) {
            wardrobeUseCase.addThingToWardrobe(userId, thingItem.id)
        } else {
            wardrobeUseCase.deleteThingFromWardrobe(userId, thingItem.id)
        }

        completable.subscribeOn(schedulers.io)
            .observeOn(schedulers.ui)
            .doOnSubscribe { _thingUpdateLiveData.value = Response.loading() }
            .subscribe(
                { _thingUpdateLiveData.value = Response.success(Unit) },
                {
                    _thingUpdateLiveData.value = Response.failure(it)
                    Timber.w(it)
                }
            )
            .addTo(disposables)
    }

}