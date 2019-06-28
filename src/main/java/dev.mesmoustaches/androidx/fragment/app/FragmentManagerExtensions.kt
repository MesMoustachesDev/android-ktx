package androidx.fragment.app

fun FragmentManager.showDialog(dialogFrag: DialogFragment, tag: String? = dialogFrag.javaClass.name): DialogFragment {
    val ft = beginTransaction()
    val prev = findFragmentByTag(tag)
    prev?.let { ft.remove(prev) }
    ft.addToBackStack(null)
    dialogFrag.show(ft, tag)
    return dialogFrag
}
