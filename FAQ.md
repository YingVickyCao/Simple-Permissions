# SimplePermissions

- Q : `new SimplePermissions(requireActivity())` has `java.lang.IllegalStateException: FragmentManager is already executing transactions`  
  A :  
  When in a fragment, should use `new SimplePermissions(this)` instead of `new SimplePermissions(fragment.requireActivity())` or `new SimplePermissions(fragment.getActivity())`,  
  or may have error `java.lang.IllegalStateException: FragmentManager is already executing transactions`.
