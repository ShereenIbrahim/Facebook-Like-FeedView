// Generated code from Butter Knife. Do not modify!
package info.androidhive.listviewfeed;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.Injector;

public class Signup$$ViewInjector<T extends info.androidhive.listviewfeed.Signup> implements Injector<T> {
  @Override public void inject(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427448, "field '_firstNameText'");
    target._firstNameText = finder.castView(view, 2131427448, "field '_firstNameText'");
    view = finder.findRequiredView(source, 2131427449, "field '_lastNameText'");
    target._lastNameText = finder.castView(view, 2131427449, "field '_lastNameText'");
    view = finder.findRequiredView(source, 2131427433, "field '_emailText'");
    target._emailText = finder.castView(view, 2131427433, "field '_emailText'");
    view = finder.findRequiredView(source, 2131427434, "field '_passwordText'");
    target._passwordText = finder.castView(view, 2131427434, "field '_passwordText'");
    view = finder.findRequiredView(source, 2131427450, "field '_signupButton'");
    target._signupButton = finder.castView(view, 2131427450, "field '_signupButton'");
    view = finder.findRequiredView(source, 2131427451, "field '_loginLink'");
    target._loginLink = finder.castView(view, 2131427451, "field '_loginLink'");
  }

  @Override public void reset(T target) {
    target._firstNameText = null;
    target._lastNameText = null;
    target._emailText = null;
    target._passwordText = null;
    target._signupButton = null;
    target._loginLink = null;
  }
}
