package com.bumptech.glide.load.resource.gifbitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.Resource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.gif.GifData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class GifBitmapWrapperTransformationTest {
    private Transformation<Bitmap> bitmapTransformation;
    private Transformation<GifData> gifTransformation;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        bitmapTransformation = mock(Transformation.class);
        gifTransformation = mock(Transformation.class);
    }

    private class BitmapResourceHarness {
        Resource<Bitmap> bitmapResource = mock(Resource.class);
        GifBitmapWrapper gifBitmapWrapper = mock(GifBitmapWrapper.class);
        Resource<GifBitmapWrapper> resource = mock(Resource.class);
        GifBitmapWrapperTransformation transformation = new GifBitmapWrapperTransformation(bitmapTransformation);
        int width = 123;
        int height = 456;

        public BitmapResourceHarness() {
            when(gifBitmapWrapper.getBitmapResource()).thenReturn(bitmapResource);
            when(resource.get()).thenReturn(gifBitmapWrapper);
        }
    }

    private class GifResourceHarness {
        GifData gifData = mock(GifData.class);
        Resource<GifData> gifResource = mock(Resource.class);
        GifBitmapWrapper gifBitmapWrapper = mock(GifBitmapWrapper.class);
        Resource<GifBitmapWrapper> resource = mock(Resource.class);
        GifBitmapWrapperTransformation transformation = new GifBitmapWrapperTransformation(null, gifTransformation);
        int width = 123;
        int height = 456;

        public GifResourceHarness() {
            when(gifResource.get()).thenReturn(gifData);
            when(gifBitmapWrapper.getGifResource()).thenReturn(gifResource);
            when(resource.get()).thenReturn(gifBitmapWrapper);
        }
    }

    @Test
    public void testHasValidId() {
        String expectedId = "asdfas";
        when(bitmapTransformation.getId()).thenReturn(expectedId);

        assertEquals(expectedId, new GifBitmapWrapperTransformation(bitmapTransformation).getId());
    }

    @Test
    public void testAppliesBitmapTransformationIfBitmapTransformationIsGivenAndResourceHasBitmapResource() {
        BitmapResourceHarness harness = new BitmapResourceHarness();

        Resource<Bitmap> transformedBitmapResource = mock(Resource.class);
        when(bitmapTransformation.transform(eq(harness.bitmapResource), eq(harness.width), eq(harness.height)))
                .thenReturn(transformedBitmapResource);
        Resource<GifBitmapWrapper> transformed = harness.transformation.transform(harness.resource, harness.width,
                harness.height);

        assertNotSame(harness.resource, transformed);
        assertEquals(transformedBitmapResource, transformed.get().getBitmapResource());
    }

    @Test
    public void testReturnsOriginalResourceIfTransformationDoesNotTransformGivenBitmapResource() {
        BitmapResourceHarness harness = new BitmapResourceHarness();

        when(bitmapTransformation.transform(eq(harness.bitmapResource), eq(harness.width), eq(harness.height)))
                .thenReturn(harness.bitmapResource);
        Resource<GifBitmapWrapper> transformed = harness.transformation.transform(harness.resource, harness.width,
                harness.height);

        assertSame(harness.resource, transformed);
    }

    @Test
    public void testReturnsOriginalResourceIfBitmapTransformationIsGivenButResourceHasNoBitmapResource() {
        BitmapResourceHarness harness = new BitmapResourceHarness();
        when(harness.gifBitmapWrapper.getBitmapResource()).thenReturn(null);

        Resource<GifBitmapWrapper> transformed = harness.transformation.transform(harness.resource, harness.width,
                harness.height);

        assertSame(harness.resource, transformed);
    }

    @Test
    public void testAppliesGifTransformationIfGifTransformationGivenAndResourceHasGifResource() {
        GifResourceHarness harness = new GifResourceHarness();
        Resource<GifData> transformedGifResource = mock(Resource.class);
        when(gifTransformation.transform(eq(harness.gifResource), eq(harness.width), eq(harness.height)))
                .thenReturn(transformedGifResource);
        Resource<GifBitmapWrapper> transformed = harness.transformation.transform(harness.resource, harness.width,
                harness.height);

        assertNotSame(harness.resource, transformed);
        assertEquals(transformedGifResource, transformed.get().getGifResource());
    }

    @Test
    public void testReturnsOriginalresourceIfTransformationDoesNotTransformGivenGifResource() {
        GifResourceHarness harness = new GifResourceHarness();
        when(gifTransformation.transform(eq(harness.gifResource), eq(harness.width), eq(harness.height)))
                .thenReturn(harness.gifResource);

        Resource<GifBitmapWrapper> transformed = harness.transformation.transform(harness.resource, harness.width,
                harness.height);

        assertSame(harness.resource, transformed);
    }

    @Test
    public void testReturnsOriginalResourceIfGifTransformationIsGivenButResourceHasNoGifResource() {
        GifResourceHarness harness = new GifResourceHarness();
        when(harness.gifBitmapWrapper.getGifResource()).thenReturn(null);

        Resource<GifBitmapWrapper> transformed = harness.transformation.transform(harness.resource, harness.width,
                harness.height);

        assertSame(harness.resource, transformed);
    }
}

