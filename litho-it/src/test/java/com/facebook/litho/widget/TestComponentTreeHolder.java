/*
 * Copyright 2018-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.litho.widget;

import static org.mockito.Mockito.mock;

import android.support.annotation.Nullable;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentTree;
import com.facebook.litho.LayoutHandler;
import com.facebook.litho.Size;
import com.facebook.litho.SizeSpec;


public class TestComponentTreeHolder extends ComponentTreeHolder {

  boolean mTreeValid;
  private ComponentTree mComponentTree;
  private RenderInfo mRenderInfo;
  boolean mLayoutAsyncCalled;
  boolean mLayoutSyncCalled;
  boolean mDidAcquireStateHandler;
  boolean mReleased;
  int mChildWidth;
  int mChildHeight;
  boolean mCheckWorkingRangeCalled;
  LayoutHandler mLayoutHandler;

  TestComponentTreeHolder(RenderInfo renderInfo) {
    mRenderInfo = renderInfo;
  }

  @Override
  public void release() {
    mReleased = true;
  }

  @Override
  public synchronized void acquireStateAndReleaseTree() {
    mComponentTree = null;
    mTreeValid = false;
    mLayoutAsyncCalled = false;
    mLayoutSyncCalled = false;
    mDidAcquireStateHandler = true;
  }

  @Override
  protected synchronized void invalidateTree() {
    mTreeValid = false;
    mLayoutAsyncCalled = false;
    mLayoutSyncCalled = false;
  }

  @Override
  public synchronized void computeLayoutAsync(
      ComponentContext context, int widthSpec, int heightSpec) {
    mComponentTree = mock(ComponentTree.class);
    mTreeValid = true;
    mLayoutAsyncCalled = true;
    mChildWidth = SizeSpec.getSize(widthSpec);
    mChildHeight = SizeSpec.getSize(heightSpec);
  }

  @Override
  public void computeLayoutSync(
      ComponentContext context, int widthSpec, int heightSpec, Size size) {
    mComponentTree = mock(ComponentTree.class);
    mTreeValid = true;
    if (size != null) {
      size.width = SizeSpec.getSize(widthSpec);
      size.height = SizeSpec.getSize(heightSpec);
    }

    mLayoutSyncCalled = true;
  }

  @Override
  public synchronized void updateLayoutHandler(@Nullable LayoutHandler layoutHandler) {
    super.updateLayoutHandler(layoutHandler);
    mLayoutHandler = layoutHandler;
  }

  @Override
  public void setRenderInfo(RenderInfo renderInfo) {
    mRenderInfo = renderInfo;
  }

  @Override
  public synchronized boolean isTreeValid() {
    return mTreeValid;
  }

  @Override
  public synchronized ComponentTree getComponentTree() {
    return mComponentTree;
  }

  @Override
  synchronized void checkWorkingRangeAndDispatch(
      int position,
      int firstVisibleIndex,
      int lastVisibleIndex,
      int firstFullyVisibleIndex,
      int lastFullyVisibleIndex) {
    mCheckWorkingRangeCalled = true;
  }

  @Override
  public RenderInfo getRenderInfo() {
    return mRenderInfo;
  }
}
