/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.openxr;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.*;

import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.*;

/**
 * <h3>Layout</h3>
 * 
 * <pre><code>
 * struct XrCompositionLayerSpaceWarpInfoFB {
 *     XrStructureType type;
 *     void const * next;
 *     XrCompositionLayerSpaceWarpInfoFlagsFB layerFlags;
 *     {@link XrSwapchainSubImage XrSwapchainSubImage} motionVectorSubImage;
 *     {@link XrPosef XrPosef} appSpaceDeltaPose;
 *     {@link XrSwapchainSubImage XrSwapchainSubImage} depthSubImage;
 *     float minDepth;
 *     float maxDepth;
 *     float nearZ;
 *     float farZ;
 * }</code></pre>
 */
public class XrCompositionLayerSpaceWarpInfoFB extends Struct implements NativeResource {

    /** The struct size in bytes. */
    public static final int SIZEOF;

    /** The struct alignment in bytes. */
    public static final int ALIGNOF;

    /** The struct member offsets. */
    public static final int
        TYPE,
        NEXT,
        LAYERFLAGS,
        MOTIONVECTORSUBIMAGE,
        APPSPACEDELTAPOSE,
        DEPTHSUBIMAGE,
        MINDEPTH,
        MAXDEPTH,
        NEARZ,
        FARZ;

    static {
        Layout layout = __struct(
            __member(4),
            __member(POINTER_SIZE),
            __member(8),
            __member(XrSwapchainSubImage.SIZEOF, XrSwapchainSubImage.ALIGNOF),
            __member(XrPosef.SIZEOF, XrPosef.ALIGNOF),
            __member(XrSwapchainSubImage.SIZEOF, XrSwapchainSubImage.ALIGNOF),
            __member(4),
            __member(4),
            __member(4),
            __member(4)
        );

        SIZEOF = layout.getSize();
        ALIGNOF = layout.getAlignment();

        TYPE = layout.offsetof(0);
        NEXT = layout.offsetof(1);
        LAYERFLAGS = layout.offsetof(2);
        MOTIONVECTORSUBIMAGE = layout.offsetof(3);
        APPSPACEDELTAPOSE = layout.offsetof(4);
        DEPTHSUBIMAGE = layout.offsetof(5);
        MINDEPTH = layout.offsetof(6);
        MAXDEPTH = layout.offsetof(7);
        NEARZ = layout.offsetof(8);
        FARZ = layout.offsetof(9);
    }

    /**
     * Creates a {@code XrCompositionLayerSpaceWarpInfoFB} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
     * visible to the struct instance and vice versa.
     *
     * <p>The created instance holds a strong reference to the container object.</p>
     */
    public XrCompositionLayerSpaceWarpInfoFB(ByteBuffer container) {
        super(memAddress(container), __checkContainer(container, SIZEOF));
    }

    @Override
    public int sizeof() { return SIZEOF; }

    /** @return the value of the {@code type} field. */
    @NativeType("XrStructureType")
    public int type() { return ntype(address()); }
    /** @return the value of the {@code next} field. */
    @NativeType("void const *")
    public long next() { return nnext(address()); }
    /** @return the value of the {@code layerFlags} field. */
    @NativeType("XrCompositionLayerSpaceWarpInfoFlagsFB")
    public long layerFlags() { return nlayerFlags(address()); }
    /** @return a {@link XrSwapchainSubImage} view of the {@code motionVectorSubImage} field. */
    public XrSwapchainSubImage motionVectorSubImage() { return nmotionVectorSubImage(address()); }
    /** @return a {@link XrPosef} view of the {@code appSpaceDeltaPose} field. */
    public XrPosef appSpaceDeltaPose() { return nappSpaceDeltaPose(address()); }
    /** @return a {@link XrSwapchainSubImage} view of the {@code depthSubImage} field. */
    public XrSwapchainSubImage depthSubImage() { return ndepthSubImage(address()); }
    /** @return the value of the {@code minDepth} field. */
    public float minDepth() { return nminDepth(address()); }
    /** @return the value of the {@code maxDepth} field. */
    public float maxDepth() { return nmaxDepth(address()); }
    /** @return the value of the {@code nearZ} field. */
    public float nearZ() { return nnearZ(address()); }
    /** @return the value of the {@code farZ} field. */
    public float farZ() { return nfarZ(address()); }

    /** Sets the specified value to the {@code type} field. */
    public XrCompositionLayerSpaceWarpInfoFB type(@NativeType("XrStructureType") int value) { ntype(address(), value); return this; }
    /** Sets the {@link FBSpaceWarp#XR_TYPE_COMPOSITION_LAYER_SPACE_WARP_INFO_FB TYPE_COMPOSITION_LAYER_SPACE_WARP_INFO_FB} value to the {@code type} field. */
    public XrCompositionLayerSpaceWarpInfoFB type$Default() { return type(FBSpaceWarp.XR_TYPE_COMPOSITION_LAYER_SPACE_WARP_INFO_FB); }
    /** Sets the specified value to the {@code next} field. */
    public XrCompositionLayerSpaceWarpInfoFB next(@NativeType("void const *") long value) { nnext(address(), value); return this; }
    /** Sets the specified value to the {@code layerFlags} field. */
    public XrCompositionLayerSpaceWarpInfoFB layerFlags(@NativeType("XrCompositionLayerSpaceWarpInfoFlagsFB") long value) { nlayerFlags(address(), value); return this; }
    /** Copies the specified {@link XrSwapchainSubImage} to the {@code motionVectorSubImage} field. */
    public XrCompositionLayerSpaceWarpInfoFB motionVectorSubImage(XrSwapchainSubImage value) { nmotionVectorSubImage(address(), value); return this; }
    /** Passes the {@code motionVectorSubImage} field to the specified {@link java.util.function.Consumer Consumer}. */
    public XrCompositionLayerSpaceWarpInfoFB motionVectorSubImage(java.util.function.Consumer<XrSwapchainSubImage> consumer) { consumer.accept(motionVectorSubImage()); return this; }
    /** Copies the specified {@link XrPosef} to the {@code appSpaceDeltaPose} field. */
    public XrCompositionLayerSpaceWarpInfoFB appSpaceDeltaPose(XrPosef value) { nappSpaceDeltaPose(address(), value); return this; }
    /** Passes the {@code appSpaceDeltaPose} field to the specified {@link java.util.function.Consumer Consumer}. */
    public XrCompositionLayerSpaceWarpInfoFB appSpaceDeltaPose(java.util.function.Consumer<XrPosef> consumer) { consumer.accept(appSpaceDeltaPose()); return this; }
    /** Copies the specified {@link XrSwapchainSubImage} to the {@code depthSubImage} field. */
    public XrCompositionLayerSpaceWarpInfoFB depthSubImage(XrSwapchainSubImage value) { ndepthSubImage(address(), value); return this; }
    /** Passes the {@code depthSubImage} field to the specified {@link java.util.function.Consumer Consumer}. */
    public XrCompositionLayerSpaceWarpInfoFB depthSubImage(java.util.function.Consumer<XrSwapchainSubImage> consumer) { consumer.accept(depthSubImage()); return this; }
    /** Sets the specified value to the {@code minDepth} field. */
    public XrCompositionLayerSpaceWarpInfoFB minDepth(float value) { nminDepth(address(), value); return this; }
    /** Sets the specified value to the {@code maxDepth} field. */
    public XrCompositionLayerSpaceWarpInfoFB maxDepth(float value) { nmaxDepth(address(), value); return this; }
    /** Sets the specified value to the {@code nearZ} field. */
    public XrCompositionLayerSpaceWarpInfoFB nearZ(float value) { nnearZ(address(), value); return this; }
    /** Sets the specified value to the {@code farZ} field. */
    public XrCompositionLayerSpaceWarpInfoFB farZ(float value) { nfarZ(address(), value); return this; }

    /** Initializes this struct with the specified values. */
    public XrCompositionLayerSpaceWarpInfoFB set(
        int type,
        long next,
        long layerFlags,
        XrSwapchainSubImage motionVectorSubImage,
        XrPosef appSpaceDeltaPose,
        XrSwapchainSubImage depthSubImage,
        float minDepth,
        float maxDepth,
        float nearZ,
        float farZ
    ) {
        type(type);
        next(next);
        layerFlags(layerFlags);
        motionVectorSubImage(motionVectorSubImage);
        appSpaceDeltaPose(appSpaceDeltaPose);
        depthSubImage(depthSubImage);
        minDepth(minDepth);
        maxDepth(maxDepth);
        nearZ(nearZ);
        farZ(farZ);

        return this;
    }

    /**
     * Copies the specified struct data to this struct.
     *
     * @param src the source struct
     *
     * @return this struct
     */
    public XrCompositionLayerSpaceWarpInfoFB set(XrCompositionLayerSpaceWarpInfoFB src) {
        memCopy(src.address(), address(), SIZEOF);
        return this;
    }

    // -----------------------------------

    /** Returns a new {@code XrCompositionLayerSpaceWarpInfoFB} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed. */
    public static XrCompositionLayerSpaceWarpInfoFB malloc() {
        return wrap(XrCompositionLayerSpaceWarpInfoFB.class, nmemAllocChecked(SIZEOF));
    }

    /** Returns a new {@code XrCompositionLayerSpaceWarpInfoFB} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed. */
    public static XrCompositionLayerSpaceWarpInfoFB calloc() {
        return wrap(XrCompositionLayerSpaceWarpInfoFB.class, nmemCallocChecked(1, SIZEOF));
    }

    /** Returns a new {@code XrCompositionLayerSpaceWarpInfoFB} instance allocated with {@link BufferUtils}. */
    public static XrCompositionLayerSpaceWarpInfoFB create() {
        ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
        return wrap(XrCompositionLayerSpaceWarpInfoFB.class, memAddress(container), container);
    }

    /** Returns a new {@code XrCompositionLayerSpaceWarpInfoFB} instance for the specified memory address. */
    public static XrCompositionLayerSpaceWarpInfoFB create(long address) {
        return wrap(XrCompositionLayerSpaceWarpInfoFB.class, address);
    }

    /** Like {@link #create(long) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrCompositionLayerSpaceWarpInfoFB createSafe(long address) {
        return address == NULL ? null : wrap(XrCompositionLayerSpaceWarpInfoFB.class, address);
    }

    /**
     * Returns a new {@link Buffer} instance allocated with {@link MemoryUtil#memAlloc memAlloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static Buffer malloc(int capacity) {
        return wrap(Buffer.class, nmemAllocChecked(__checkMalloc(capacity, SIZEOF)), capacity);
    }

    /**
     * Returns a new {@link Buffer} instance allocated with {@link MemoryUtil#memCalloc memCalloc}. The instance must be explicitly freed.
     *
     * @param capacity the buffer capacity
     */
    public static Buffer calloc(int capacity) {
        return wrap(Buffer.class, nmemCallocChecked(capacity, SIZEOF), capacity);
    }

    /**
     * Returns a new {@link Buffer} instance allocated with {@link BufferUtils}.
     *
     * @param capacity the buffer capacity
     */
    public static Buffer create(int capacity) {
        ByteBuffer container = __create(capacity, SIZEOF);
        return wrap(Buffer.class, memAddress(container), capacity, container);
    }

    /**
     * Create a {@link Buffer} instance at the specified memory.
     *
     * @param address  the memory address
     * @param capacity the buffer capacity
     */
    public static Buffer create(long address, int capacity) {
        return wrap(Buffer.class, address, capacity);
    }

    /** Like {@link #create(long, int) create}, but returns {@code null} if {@code address} is {@code NULL}. */
    @Nullable
    public static XrCompositionLayerSpaceWarpInfoFB.Buffer createSafe(long address, int capacity) {
        return address == NULL ? null : wrap(Buffer.class, address, capacity);
    }


    /**
     * Returns a new {@code XrCompositionLayerSpaceWarpInfoFB} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack the stack from which to allocate
     */
    public static XrCompositionLayerSpaceWarpInfoFB malloc(MemoryStack stack) {
        return wrap(XrCompositionLayerSpaceWarpInfoFB.class, stack.nmalloc(ALIGNOF, SIZEOF));
    }

    /**
     * Returns a new {@code XrCompositionLayerSpaceWarpInfoFB} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack the stack from which to allocate
     */
    public static XrCompositionLayerSpaceWarpInfoFB calloc(MemoryStack stack) {
        return wrap(XrCompositionLayerSpaceWarpInfoFB.class, stack.ncalloc(ALIGNOF, 1, SIZEOF));
    }

    /**
     * Returns a new {@link Buffer} instance allocated on the specified {@link MemoryStack}.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static Buffer malloc(int capacity, MemoryStack stack) {
        return wrap(Buffer.class, stack.nmalloc(ALIGNOF, capacity * SIZEOF), capacity);
    }

    /**
     * Returns a new {@link Buffer} instance allocated on the specified {@link MemoryStack} and initializes all its bits to zero.
     *
     * @param stack    the stack from which to allocate
     * @param capacity the buffer capacity
     */
    public static Buffer calloc(int capacity, MemoryStack stack) {
        return wrap(Buffer.class, stack.ncalloc(ALIGNOF, capacity, SIZEOF), capacity);
    }

    // -----------------------------------

    /** Unsafe version of {@link #type}. */
    public static int ntype(long struct) { return UNSAFE.getInt(null, struct + XrCompositionLayerSpaceWarpInfoFB.TYPE); }
    /** Unsafe version of {@link #next}. */
    public static long nnext(long struct) { return memGetAddress(struct + XrCompositionLayerSpaceWarpInfoFB.NEXT); }
    /** Unsafe version of {@link #layerFlags}. */
    public static long nlayerFlags(long struct) { return UNSAFE.getLong(null, struct + XrCompositionLayerSpaceWarpInfoFB.LAYERFLAGS); }
    /** Unsafe version of {@link #motionVectorSubImage}. */
    public static XrSwapchainSubImage nmotionVectorSubImage(long struct) { return XrSwapchainSubImage.create(struct + XrCompositionLayerSpaceWarpInfoFB.MOTIONVECTORSUBIMAGE); }
    /** Unsafe version of {@link #appSpaceDeltaPose}. */
    public static XrPosef nappSpaceDeltaPose(long struct) { return XrPosef.create(struct + XrCompositionLayerSpaceWarpInfoFB.APPSPACEDELTAPOSE); }
    /** Unsafe version of {@link #depthSubImage}. */
    public static XrSwapchainSubImage ndepthSubImage(long struct) { return XrSwapchainSubImage.create(struct + XrCompositionLayerSpaceWarpInfoFB.DEPTHSUBIMAGE); }
    /** Unsafe version of {@link #minDepth}. */
    public static float nminDepth(long struct) { return UNSAFE.getFloat(null, struct + XrCompositionLayerSpaceWarpInfoFB.MINDEPTH); }
    /** Unsafe version of {@link #maxDepth}. */
    public static float nmaxDepth(long struct) { return UNSAFE.getFloat(null, struct + XrCompositionLayerSpaceWarpInfoFB.MAXDEPTH); }
    /** Unsafe version of {@link #nearZ}. */
    public static float nnearZ(long struct) { return UNSAFE.getFloat(null, struct + XrCompositionLayerSpaceWarpInfoFB.NEARZ); }
    /** Unsafe version of {@link #farZ}. */
    public static float nfarZ(long struct) { return UNSAFE.getFloat(null, struct + XrCompositionLayerSpaceWarpInfoFB.FARZ); }

    /** Unsafe version of {@link #type(int) type}. */
    public static void ntype(long struct, int value) { UNSAFE.putInt(null, struct + XrCompositionLayerSpaceWarpInfoFB.TYPE, value); }
    /** Unsafe version of {@link #next(long) next}. */
    public static void nnext(long struct, long value) { memPutAddress(struct + XrCompositionLayerSpaceWarpInfoFB.NEXT, value); }
    /** Unsafe version of {@link #layerFlags(long) layerFlags}. */
    public static void nlayerFlags(long struct, long value) { UNSAFE.putLong(null, struct + XrCompositionLayerSpaceWarpInfoFB.LAYERFLAGS, value); }
    /** Unsafe version of {@link #motionVectorSubImage(XrSwapchainSubImage) motionVectorSubImage}. */
    public static void nmotionVectorSubImage(long struct, XrSwapchainSubImage value) { memCopy(value.address(), struct + XrCompositionLayerSpaceWarpInfoFB.MOTIONVECTORSUBIMAGE, XrSwapchainSubImage.SIZEOF); }
    /** Unsafe version of {@link #appSpaceDeltaPose(XrPosef) appSpaceDeltaPose}. */
    public static void nappSpaceDeltaPose(long struct, XrPosef value) { memCopy(value.address(), struct + XrCompositionLayerSpaceWarpInfoFB.APPSPACEDELTAPOSE, XrPosef.SIZEOF); }
    /** Unsafe version of {@link #depthSubImage(XrSwapchainSubImage) depthSubImage}. */
    public static void ndepthSubImage(long struct, XrSwapchainSubImage value) { memCopy(value.address(), struct + XrCompositionLayerSpaceWarpInfoFB.DEPTHSUBIMAGE, XrSwapchainSubImage.SIZEOF); }
    /** Unsafe version of {@link #minDepth(float) minDepth}. */
    public static void nminDepth(long struct, float value) { UNSAFE.putFloat(null, struct + XrCompositionLayerSpaceWarpInfoFB.MINDEPTH, value); }
    /** Unsafe version of {@link #maxDepth(float) maxDepth}. */
    public static void nmaxDepth(long struct, float value) { UNSAFE.putFloat(null, struct + XrCompositionLayerSpaceWarpInfoFB.MAXDEPTH, value); }
    /** Unsafe version of {@link #nearZ(float) nearZ}. */
    public static void nnearZ(long struct, float value) { UNSAFE.putFloat(null, struct + XrCompositionLayerSpaceWarpInfoFB.NEARZ, value); }
    /** Unsafe version of {@link #farZ(float) farZ}. */
    public static void nfarZ(long struct, float value) { UNSAFE.putFloat(null, struct + XrCompositionLayerSpaceWarpInfoFB.FARZ, value); }

    /**
     * Validates pointer members that should not be {@code NULL}.
     *
     * @param struct the struct to validate
     */
    public static void validate(long struct) {
        XrSwapchainSubImage.validate(struct + XrCompositionLayerSpaceWarpInfoFB.MOTIONVECTORSUBIMAGE);
        XrSwapchainSubImage.validate(struct + XrCompositionLayerSpaceWarpInfoFB.DEPTHSUBIMAGE);
    }

    /**
     * Calls {@link #validate(long)} for each struct contained in the specified struct array.
     *
     * @param array the struct array to validate
     * @param count the number of structs in {@code array}
     */
    public static void validate(long array, int count) {
        for (int i = 0; i < count; i++) {
            validate(array + Integer.toUnsignedLong(i) * SIZEOF);
        }
    }

    // -----------------------------------

    /** An array of {@link XrCompositionLayerSpaceWarpInfoFB} structs. */
    public static class Buffer extends StructBuffer<XrCompositionLayerSpaceWarpInfoFB, Buffer> implements NativeResource {

        private static final XrCompositionLayerSpaceWarpInfoFB ELEMENT_FACTORY = XrCompositionLayerSpaceWarpInfoFB.create(-1L);

        /**
         * Creates a new {@code XrCompositionLayerSpaceWarpInfoFB.Buffer} instance backed by the specified container.
         *
         * Changes to the container's content will be visible to the struct buffer instance and vice versa. The two buffers' position, limit, and mark values
         * will be independent. The new buffer's position will be zero, its capacity and its limit will be the number of bytes remaining in this buffer divided
         * by {@link XrCompositionLayerSpaceWarpInfoFB#SIZEOF}, and its mark will be undefined.
         *
         * <p>The created buffer instance holds a strong reference to the container object.</p>
         */
        public Buffer(ByteBuffer container) {
            super(container, container.remaining() / SIZEOF);
        }

        public Buffer(long address, int cap) {
            super(address, null, -1, 0, cap, cap);
        }

        Buffer(long address, @Nullable ByteBuffer container, int mark, int pos, int lim, int cap) {
            super(address, container, mark, pos, lim, cap);
        }

        @Override
        protected Buffer self() {
            return this;
        }

        @Override
        protected XrCompositionLayerSpaceWarpInfoFB getElementFactory() {
            return ELEMENT_FACTORY;
        }

        /** @return the value of the {@code type} field. */
        @NativeType("XrStructureType")
        public int type() { return XrCompositionLayerSpaceWarpInfoFB.ntype(address()); }
        /** @return the value of the {@code next} field. */
        @NativeType("void const *")
        public long next() { return XrCompositionLayerSpaceWarpInfoFB.nnext(address()); }
        /** @return the value of the {@code layerFlags} field. */
        @NativeType("XrCompositionLayerSpaceWarpInfoFlagsFB")
        public long layerFlags() { return XrCompositionLayerSpaceWarpInfoFB.nlayerFlags(address()); }
        /** @return a {@link XrSwapchainSubImage} view of the {@code motionVectorSubImage} field. */
        public XrSwapchainSubImage motionVectorSubImage() { return XrCompositionLayerSpaceWarpInfoFB.nmotionVectorSubImage(address()); }
        /** @return a {@link XrPosef} view of the {@code appSpaceDeltaPose} field. */
        public XrPosef appSpaceDeltaPose() { return XrCompositionLayerSpaceWarpInfoFB.nappSpaceDeltaPose(address()); }
        /** @return a {@link XrSwapchainSubImage} view of the {@code depthSubImage} field. */
        public XrSwapchainSubImage depthSubImage() { return XrCompositionLayerSpaceWarpInfoFB.ndepthSubImage(address()); }
        /** @return the value of the {@code minDepth} field. */
        public float minDepth() { return XrCompositionLayerSpaceWarpInfoFB.nminDepth(address()); }
        /** @return the value of the {@code maxDepth} field. */
        public float maxDepth() { return XrCompositionLayerSpaceWarpInfoFB.nmaxDepth(address()); }
        /** @return the value of the {@code nearZ} field. */
        public float nearZ() { return XrCompositionLayerSpaceWarpInfoFB.nnearZ(address()); }
        /** @return the value of the {@code farZ} field. */
        public float farZ() { return XrCompositionLayerSpaceWarpInfoFB.nfarZ(address()); }

        /** Sets the specified value to the {@code type} field. */
        public Buffer type(@NativeType("XrStructureType") int value) { XrCompositionLayerSpaceWarpInfoFB.ntype(address(), value); return this; }
        /** Sets the {@link FBSpaceWarp#XR_TYPE_COMPOSITION_LAYER_SPACE_WARP_INFO_FB TYPE_COMPOSITION_LAYER_SPACE_WARP_INFO_FB} value to the {@code type} field. */
        public Buffer type$Default() { return type(FBSpaceWarp.XR_TYPE_COMPOSITION_LAYER_SPACE_WARP_INFO_FB); }
        /** Sets the specified value to the {@code next} field. */
        public Buffer next(@NativeType("void const *") long value) { XrCompositionLayerSpaceWarpInfoFB.nnext(address(), value); return this; }
        /** Sets the specified value to the {@code layerFlags} field. */
        public Buffer layerFlags(@NativeType("XrCompositionLayerSpaceWarpInfoFlagsFB") long value) { XrCompositionLayerSpaceWarpInfoFB.nlayerFlags(address(), value); return this; }
        /** Copies the specified {@link XrSwapchainSubImage} to the {@code motionVectorSubImage} field. */
        public Buffer motionVectorSubImage(XrSwapchainSubImage value) { XrCompositionLayerSpaceWarpInfoFB.nmotionVectorSubImage(address(), value); return this; }
        /** Passes the {@code motionVectorSubImage} field to the specified {@link java.util.function.Consumer Consumer}. */
        public Buffer motionVectorSubImage(java.util.function.Consumer<XrSwapchainSubImage> consumer) { consumer.accept(motionVectorSubImage()); return this; }
        /** Copies the specified {@link XrPosef} to the {@code appSpaceDeltaPose} field. */
        public Buffer appSpaceDeltaPose(XrPosef value) { XrCompositionLayerSpaceWarpInfoFB.nappSpaceDeltaPose(address(), value); return this; }
        /** Passes the {@code appSpaceDeltaPose} field to the specified {@link java.util.function.Consumer Consumer}. */
        public Buffer appSpaceDeltaPose(java.util.function.Consumer<XrPosef> consumer) { consumer.accept(appSpaceDeltaPose()); return this; }
        /** Copies the specified {@link XrSwapchainSubImage} to the {@code depthSubImage} field. */
        public Buffer depthSubImage(XrSwapchainSubImage value) { XrCompositionLayerSpaceWarpInfoFB.ndepthSubImage(address(), value); return this; }
        /** Passes the {@code depthSubImage} field to the specified {@link java.util.function.Consumer Consumer}. */
        public Buffer depthSubImage(java.util.function.Consumer<XrSwapchainSubImage> consumer) { consumer.accept(depthSubImage()); return this; }
        /** Sets the specified value to the {@code minDepth} field. */
        public Buffer minDepth(float value) { XrCompositionLayerSpaceWarpInfoFB.nminDepth(address(), value); return this; }
        /** Sets the specified value to the {@code maxDepth} field. */
        public Buffer maxDepth(float value) { XrCompositionLayerSpaceWarpInfoFB.nmaxDepth(address(), value); return this; }
        /** Sets the specified value to the {@code nearZ} field. */
        public Buffer nearZ(float value) { XrCompositionLayerSpaceWarpInfoFB.nnearZ(address(), value); return this; }
        /** Sets the specified value to the {@code farZ} field. */
        public Buffer farZ(float value) { XrCompositionLayerSpaceWarpInfoFB.nfarZ(address(), value); return this; }

    }

}