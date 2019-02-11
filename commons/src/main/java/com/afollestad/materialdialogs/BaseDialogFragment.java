package com.afollestad.materialdialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class BaseDialogFragment<T extends BaseDialogFragment.Builder> extends DialogFragment {

    protected T mBuilder;

    @NonNull
    @Override
    @CallSuper
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (mBuilder == null) {
            throw new IllegalStateException("You must create this class using the Builder");
        }
        return super.onCreateDialog(savedInstanceState);
    }

    protected static void bindBuilder(BaseDialogFragment dialogFragment, @NonNull Builder builder) {
        dialogFragment.mBuilder = builder;
    }

    public void show() {
        final FragmentManager fragmentManager = mBuilder.fragmentManager;

        final String tag = mBuilder.tag;
        Fragment frag = fragmentManager.findFragmentByTag(tag);
        if (frag != null) {
            ((DialogFragment) frag).dismiss();
            fragmentManager.beginTransaction().remove(frag).commit();
        }

        show(fragmentManager, tag);
    }

    public static abstract class Builder {

        protected FragmentManager fragmentManager;
        protected String tag;

        public Builder(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        /**
         * @param dialogFragment a clean fragment of the type T
         * @param <T> the class of the dialog fragment to build
         * @return the dialogFragment
         */
        @CallSuper
        public <T extends BaseDialogFragment> T build(T dialogFragment) {
            bindBuilder(dialogFragment, this);
            return dialogFragment;
        }

        /**
         * @param dialogFragment a clean fragment of the type T
         * @param <T> the class of the dialog fragment to show
         * @return the dialogFragment
         */
        @CallSuper
        public <T extends BaseDialogFragment> T show(T dialogFragment) {
            build(dialogFragment)
                    .show();
            return dialogFragment;
        }
    }
}
