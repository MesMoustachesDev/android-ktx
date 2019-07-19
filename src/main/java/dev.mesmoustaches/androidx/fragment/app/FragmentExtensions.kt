package androidx.fragment.app

import android.view.View

inline fun FragmentManager.fragmentTransaction(transaction: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.transaction()
    fragmentTransaction.commit()
}

fun Fragment.addNotificationHeightPadding(view: View) {
    view.setPadding(0, getStatusBarHeight(), 0, 0)
}

fun Fragment.getStatusBarHeight(): Int {
    // A method to find height of the status bar
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

//inline fun <reified T : ViewModel> Fragment.parentViewModel(
//        key: String? = null,
//        name: String? = null,
//        noinline from: ViewModelStoreOwnerDefinition = { (parentFragment ?: activity) as ViewModelStoreOwner },
//        noinline parameters: ParameterDefinition = org.koin.core.parameter.emptyParameterDefinition()
//) = viewModelByClass(T::class, key, name, from, parameters)
//
//inline fun <reified T : ViewModel> Fragment.rootFragmentViewModel(
//        key: String? = null,
//        name: String? = null,
//        noinline from: ViewModelStoreOwnerDefinition = {
//            var parent:Fragment? = this
//            while (parent?.parentFragment != null) parent = parent?.parentFragment
//
//            parent as ViewModelStoreOwner
//        },
//        noinline parameters: ParameterDefinition = org.koin.core.parameter.emptyParameterDefinition()
//) = viewModelByClass(T::class, key, name, from, parameters)