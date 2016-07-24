/*
 * Copyright LWJGL. All rights reserved.
 * License terms: http://lwjgl.org/license.php
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.opengl;

import java.nio.*;

import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.JNI.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * Native bindings to the <a href="http://www.opengl.org/registry/specs/ARB/tessellation_shader.txt">ARB_tessellation_shader</a> extension.
 * 
 * <p>This extension introduces new tessellation stages and two new shader types to the OpenGL primitive processing pipeline. These pipeline stages operate on
 * a new basic primitive type, called a patch. A patch consists of a fixed-size collection of vertices, each with per-vertex attributes, plus a number of
 * associated per-patch attributes. Tessellation control shaders transform an input patch specified by the application, computing per-vertex and per-patch
 * attributes for a new output patch. A fixed-function tessellation primitive generator subdivides the patch, and tessellation evaluation shaders are used
 * to compute the position and attributes of each vertex produced by the tessellator.</p>
 * 
 * <p>When tessellation is active, it begins by running the optional tessellation control shader. This shader consumes an input patch and produces a new
 * fixed-size output patch. The output patch consists of an array of vertices, and a set of per-patch attributes. The per-patch attributes include
 * tessellation levels that control how finely the patch will be tessellated. For each patch processed, multiple tessellation control shader invocations
 * are performed -- one per output patch vertex. Each tessellation control shader invocation writes all the attributes of its corresponding output patch
 * vertex. A tessellation control shader may also read the per-vertex outputs of other tessellation control shader invocations, as well as read and write
 * shared per-patch outputs. The tessellation control shader invocations for a single patch effectively run as a group. A built-in {@code barrier()}
 * function is provided to allow synchronization points where no shader invocation will continue until all shader invocations have reached the barrier.</p>
 * 
 * <p>The tessellation primitive generator then decomposes a patch into a new set of primitives using the tessellation levels to determine how finely
 * tessellated the output should be. The primitive generator begins with either a triangle or a quad, and splits each outer edge of the primitive into a
 * number of segments approximately equal to the corresponding element of the outer tessellation level array. The interior of the primitive is tessellated
 * according to elements of the inner tessellation level array. The primitive generator has three modes: "triangles" and "quads" split a triangular or
 * quad-shaped patch into a set of triangles that cover the original patch; "isolines" splits a quad-shaped patch into a set of line strips running across
 * the patch horizontally. Each vertex generated by the tessellation primitive generator is assigned a (u,v) or (u,v,w) coordinate indicating its relative
 * location in the subdivided triangle or quad.</p>
 * 
 * <p>For each vertex produced by the tessellation primitive generator, the tessellation evaluation shader is run to compute its position and other attributes
 * of the vertex, using its (u,v) or (u,v,w) coordinate. When computing final vertex attributes, the tessellation evaluation shader can also read the
 * attributes of any of the vertices of the patch written by the tessellation control shader. Tessellation evaluation shader invocations are completely
 * independent, although all invocations for a single patch share the same collection of input vertices and per-patch attributes.</p>
 * 
 * <p>The tessellator operates on vertices after they have been transformed by a vertex shader. The primitives generated by the tessellator are passed further
 * down the OpenGL pipeline, where they can be used as inputs to geometry shaders, transform feedback, and the rasterizer.</p>
 * 
 * <p>The tessellation control and evaluation shaders are both optional. If neither shader type is present, the tessellation stage has no effect. If no
 * tessellation control shader is present, the input patch provided by the application is passed directly to the tessellation primitive generator, and a
 * set of default tessellation level parameters is used to control primitive generation. In this extension, patches may not be passed beyond the
 * tessellation evaluation shader, and an error is generated if an application provides patches and the current program object contains no tessellation
 * evaluation shader.</p>
 * 
 * <p>Requires {@link GL32 GL32} and GLSL 1.50. Promoted to core in {@link GL40 OpenGL 4.0}.</p>
 */
public class ARBTessellationShader {

	/** Accepted by the {@code mode} parameter of Begin and all vertex array functions that implicitly call Begin. */
	public static final int GL_PATCHES = 0xE;

	/** Accepted by the {@code pname} parameter of PatchParameteri, GetBooleanv, GetDoublev, GetFloatv, GetIntegerv, and GetInteger64v. */
	public static final int GL_PATCH_VERTICES = 0x8E72;

	/** Accepted by the {@code pname} parameter of PatchParameterfv, GetBooleanv, GetDoublev, GetFloatv, and GetIntegerv, and GetInteger64v. */
	public static final int
		GL_PATCH_DEFAULT_INNER_LEVEL = 0x8E73,
		GL_PATCH_DEFAULT_OUTER_LEVEL = 0x8E74;

	/** Accepted by the {@code pname} parameter of GetProgramiv. */
	public static final int
		GL_TESS_CONTROL_OUTPUT_VERTICES = 0x8E75,
		GL_TESS_GEN_MODE                = 0x8E76,
		GL_TESS_GEN_SPACING             = 0x8E77,
		GL_TESS_GEN_VERTEX_ORDER        = 0x8E78,
		GL_TESS_GEN_POINT_MODE          = 0x8E79;

	/** Returned by GetProgramiv when {@code pname} is TESS_GEN_MODE. */
	public static final int GL_ISOLINES = 0x8E7A;

	/** Returned by GetProgramiv when {@code pname} is TESS_GEN_SPACING. */
	public static final int
		GL_FRACTIONAL_ODD  = 0x8E7B,
		GL_FRACTIONAL_EVEN = 0x8E7C;

	/** Accepted by the {@code pname} parameter of GetBooleanv, GetDoublev, GetFloatv, GetIntegerv, and GetInteger64v. */
	public static final int
		GL_MAX_PATCH_VERTICES                              = 0x8E7D,
		GL_MAX_TESS_GEN_LEVEL                              = 0x8E7E,
		GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS             = 0x8E7F,
		GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS          = 0x8E80,
		GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS            = 0x8E81,
		GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS         = 0x8E82,
		GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS              = 0x8E83,
		GL_MAX_TESS_PATCH_COMPONENTS                       = 0x8E84,
		GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS        = 0x8E85,
		GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS           = 0x8E86,
		GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS                 = 0x8E89,
		GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS              = 0x8E8A,
		GL_MAX_TESS_CONTROL_INPUT_COMPONENTS               = 0x886C,
		GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS            = 0x886D,
		GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS    = 0x8E1E,
		GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8E1F;

	/** Accepted by the {@code pname} parameter of GetActiveUniformBlockiv. */
	public static final int
		GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER    = 0x84F0,
		GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x84F1;

	/** Accepted by the {@code type} parameter of CreateShader and returned by the {@code params} parameter of GetShaderiv. */
	public static final int
		GL_TESS_EVALUATION_SHADER = 0x8E87,
		GL_TESS_CONTROL_SHADER    = 0x8E88;

	protected ARBTessellationShader() {
		throw new UnsupportedOperationException();
	}

	static boolean isAvailable(GLCapabilities caps) {
		return checkFunctions(
			caps.glPatchParameteri, caps.glPatchParameterfv
		);
	}

	// --- [ glPatchParameteri ] ---

	/**
	 * Specifies the integer value of the specified parameter for patch primitives.
	 *
	 * @param pname the name of the parameter to set. Must be:<br><table><tr><td>{@link #GL_PATCH_VERTICES PATCH_VERTICES}</td></tr></table>
	 * @param value the new value for the parameter given by {@code pname}
	 */
	public static void glPatchParameteri(int pname, int value) {
		long __functionAddress = GL.getCapabilities().glPatchParameteri;
		if ( CHECKS )
			checkFunctionAddress(__functionAddress);
		callV(__functionAddress, pname, value);
	}

	// --- [ glPatchParameterfv ] ---

	/**
	 * Specifies an array of float values for the specified parameter for patch primitives.
	 *
	 * @param pname  the name of the parameter to set. One of:<br><table><tr><td>{@link #GL_PATCH_DEFAULT_OUTER_LEVEL PATCH_DEFAULT_OUTER_LEVEL}</td><td>{@link #GL_PATCH_DEFAULT_INNER_LEVEL PATCH_DEFAULT_INNER_LEVEL}</td></tr></table>
	 * @param values an array containing the new values for the parameter given by {@code pname}
	 */
	public static void nglPatchParameterfv(int pname, long values) {
		long __functionAddress = GL.getCapabilities().glPatchParameterfv;
		if ( CHECKS )
			checkFunctionAddress(__functionAddress);
		callPV(__functionAddress, pname, values);
	}

	/**
	 * Specifies an array of float values for the specified parameter for patch primitives.
	 *
	 * @param pname  the name of the parameter to set. One of:<br><table><tr><td>{@link #GL_PATCH_DEFAULT_OUTER_LEVEL PATCH_DEFAULT_OUTER_LEVEL}</td><td>{@link #GL_PATCH_DEFAULT_INNER_LEVEL PATCH_DEFAULT_INNER_LEVEL}</td></tr></table>
	 * @param values an array containing the new values for the parameter given by {@code pname}
	 */
	public static void glPatchParameterfv(int pname, FloatBuffer values) {
		if ( CHECKS )
			if ( DEBUG )
				checkBuffer(values, GL11.glGetInteger(GL_PATCH_VERTICES));
		nglPatchParameterfv(pname, memAddress(values));
	}

	/** Array version of: {@link #glPatchParameterfv PatchParameterfv} */
	public static void glPatchParameterfv(int pname, float[] values) {
		long __functionAddress = GL.getCapabilities().glPatchParameterfv;
		if ( CHECKS ) {
			checkFunctionAddress(__functionAddress);
			if ( DEBUG )
				checkBuffer(values, GL11.glGetInteger(GL_PATCH_VERTICES));
		}
		callPV(__functionAddress, pname, values);
	}

}