package com.sedsoftware.bakingapp.features.recipestep;

import com.sedsoftware.bakingapp.data.source.RecipeRepository;
import com.sedsoftware.bakingapp.features.recipestep.RecipeStepContract.View;
import com.sedsoftware.bakingapp.utils.RxUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import javax.inject.Inject;

public class RecipeStepPresenter implements RecipeStepContract.Presenter {

  private final RecipeRepository recipeRepository;
  private final RecipeStepContract.View stepView;
  private int recipeId;

  private CompositeDisposable disposableList;

  @Inject
  public RecipeStepPresenter(RecipeRepository recipeRepository,
      View stepView, int recipeId) {
    this.recipeRepository = recipeRepository;
    this.stepView = stepView;
    this.recipeId = recipeId;

    disposableList = new CompositeDisposable();
  }

  @Inject
  void setupListeners() {
    stepView.setPresenter(this);
  }

  @Override
  public void subscribe() {
    loadStepsToAdapter();
  }

  @Override
  public void unsubscribe() {
    disposableList.clear();
  }

  @Override
  public void loadStepsToAdapter() {
    Disposable subscription = recipeRepository
        .getRecipeSteps(recipeId)
        .compose(RxUtils.applySchedulers())
        .subscribe(
            // OnNext
            stepView::showStepsInViewpager,
            // OnError
            throwable -> stepView.showErrorMessage());

    disposableList.add(subscription);
  }
}