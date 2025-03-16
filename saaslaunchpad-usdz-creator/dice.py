#!/usr/bin/env python

from pxr import Usd, UsdGeom, UsdShade, Sdf, Gf, Kind
import numpy as np

def create_elegant_dice(output_file="elegant_dice.usda"):
    """
    Creates a 3D dice with an elegant design inspired by the purple/navy circular logo.
    Each face has a different symbol.
    """
    # Create the USD stage
    stage = Usd.Stage.CreateNew(output_file)
    
    # Define up-axis
    UsdGeom.SetStageUpAxis(stage, UsdGeom.Tokens.y)
    
    # Create root xform
    root_prim = UsdGeom.Xform.Define(stage, "/ElegantDice")
    Usd.ModelAPI(root_prim).SetKind(Kind.Tokens.component)
    
    # Create a cube (dice) mesh
    dice = UsdGeom.Mesh.Define(stage, "/ElegantDice/DiceMesh")
    
    # Define the cube
    size = 2.0
    half_size = size / 2.0
    
    # Define vertices of a cube
    points = [
        Gf.Vec3f(-half_size, -half_size, -half_size),  # 0
        Gf.Vec3f(half_size, -half_size, -half_size),   # 1
        Gf.Vec3f(half_size, -half_size, half_size),    # 2
        Gf.Vec3f(-half_size, -half_size, half_size),   # 3
        Gf.Vec3f(-half_size, half_size, -half_size),   # 4
        Gf.Vec3f(half_size, half_size, -half_size),    # 5
        Gf.Vec3f(half_size, half_size, half_size),     # 6
        Gf.Vec3f(-half_size, half_size, half_size)     # 7
    ]
    
    # Define faces (each face is a quad)
    face_vertex_counts = [4, 4, 4, 4, 4, 4]  # 6 faces, 4 vertices each
    
    # Define vertex indices for each face
    face_vertex_indices = [
        0, 1, 5, 4,  # back
        1, 2, 6, 5,  # right
        2, 3, 7, 6,  # front
        3, 0, 4, 7,  # left
        4, 5, 6, 7,  # top
        0, 3, 2, 1   # bottom
    ]
    
    # Set the mesh geometry
    dice.CreatePointsAttr(points)
    dice.CreateFaceVertexCountsAttr(face_vertex_counts)
    dice.CreateFaceVertexIndicesAttr(face_vertex_indices)
    
    # Define UV coordinates for texturing
    st_values = [
        Gf.Vec2f(0, 0), Gf.Vec2f(1, 0), Gf.Vec2f(1, 1), Gf.Vec2f(0, 1),  # back face
        Gf.Vec2f(0, 0), Gf.Vec2f(1, 0), Gf.Vec2f(1, 1), Gf.Vec2f(0, 1),  # right face
        Gf.Vec2f(0, 0), Gf.Vec2f(1, 0), Gf.Vec2f(1, 1), Gf.Vec2f(0, 1),  # front face
        Gf.Vec2f(0, 0), Gf.Vec2f(1, 0), Gf.Vec2f(1, 1), Gf.Vec2f(0, 1),  # left face
        Gf.Vec2f(0, 0), Gf.Vec2f(1, 0), Gf.Vec2f(1, 1), Gf.Vec2f(0, 1),  # top face
        Gf.Vec2f(0, 0), Gf.Vec2f(1, 0), Gf.Vec2f(1, 1), Gf.Vec2f(0, 1),  # bottom face
    ]
    st_indices = list(range(24))  # 6 faces * 4 verts = 24 indices
    
    # Set UV coordinates
    primvarsAPI = UsdGeom.PrimvarsAPI(dice)
    texCoordPrimvar = primvarsAPI.CreatePrimvar("st", 
                                              Sdf.ValueTypeNames.TexCoord2fArray, 
                                              UsdGeom.Tokens.varying)
    texCoordPrimvar.Set(st_values)
    texCoordPrimvar.SetIndices(st_indices)
    
    # Create materials for the dice
    material = UsdShade.Material.Define(stage, '/ElegantDice/Material')
    
    # Create a PBR shader
    pbrShader = UsdShade.Shader.Define(stage, '/ElegantDice/Material/PBRShader')
    pbrShader.CreateIdAttr("UsdPreviewSurface")
    
    # Set the shader properties for an elegant, glossy look
    pbrShader.CreateInput("diffuseColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(0.2, 0.1, 0.3))  # Deep purple
    pbrShader.CreateInput("metallic", Sdf.ValueTypeNames.Float).Set(0.7)
    pbrShader.CreateInput("roughness", Sdf.ValueTypeNames.Float).Set(0.1)  # Very glossy
    pbrShader.CreateInput("specularColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(0.9, 0.8, 1.0))  # Light purple specular
    pbrShader.CreateInput("emissiveColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(0.05, 0.02, 0.1))  # Subtle glow
    
    # Connect the PBR shader to the material output
    material.CreateSurfaceOutput().ConnectToSource(pbrShader, "surface")
    
    # Bind the material to the mesh
    UsdShade.MaterialBindingAPI(dice).Bind(material)
    
    # Create a separate mesh for each symbol on the dice faces
    create_face_symbols(stage, root_prim, size)
    
    # Set the time range for animation (if needed)
    stage.SetStartTimeCode(1)
    stage.SetEndTimeCode(24)
    
    # Save the USD file
    stage.GetRootLayer().Save()
    print(f"Saved USD file to {output_file}")
    
    return stage

def create_face_symbols(stage, root_prim, size):
    """Create the different symbols for each face of the dice"""
    half_size = size / 2.0
    offset = 0.01  # Slight offset from the face to avoid z-fighting
    
    # Face 1: Three lines (like in the logo)
    lines_xform = UsdGeom.Xform.Define(stage, "/ElegantDice/Symbols/ThreeLines")
    
    # Create each line as a separate mesh
    for i in range(3):
        line = UsdGeom.Mesh.Define(stage, f"/ElegantDice/Symbols/ThreeLines/Line{i+1}")
        
        # Position vertically with spacing
        y_pos = 0.3 * (i - 1)
        
        # Create the line mesh
        line_width = 0.6
        line_height = 0.1
        line_depth = 0.02
        
        points = [
            Gf.Vec3f(-line_width/2, y_pos - line_height/2, half_size + offset),
            Gf.Vec3f(line_width/2, y_pos - line_height/2, half_size + offset),
            Gf.Vec3f(line_width/2, y_pos + line_height/2, half_size + offset),
            Gf.Vec3f(-line_width/2, y_pos + line_height/2, half_size + offset),
            
            Gf.Vec3f(-line_width/2, y_pos - line_height/2, half_size + offset + line_depth),
            Gf.Vec3f(line_width/2, y_pos - line_height/2, half_size + offset + line_depth),
            Gf.Vec3f(line_width/2, y_pos + line_height/2, half_size + offset + line_depth),
            Gf.Vec3f(-line_width/2, y_pos + line_height/2, half_size + offset + line_depth),
        ]
        
        line.CreatePointsAttr(points)
        
        # Define faces (each face is a quad)
        face_vertex_counts = [4, 4, 4, 4, 4, 4]  # 6 faces, 4 vertices each
        
        # Define vertex indices for each face
        face_vertex_indices = [
            0, 1, 2, 3,  # front
            4, 5, 6, 7,  # back
            0, 3, 7, 4,  # left
            1, 2, 6, 5,  # right
            0, 1, 5, 4,  # bottom
            3, 2, 6, 7   # top
        ]
        
        line.CreateFaceVertexCountsAttr(face_vertex_counts)
        line.CreateFaceVertexIndicesAttr(face_vertex_indices)
        
        # Create and bind material
        line_material = UsdShade.Material.Define(stage, f'/ElegantDice/Symbols/ThreeLines/Line{i+1}/Material')
        line_shader = UsdShade.Shader.Define(stage, f'/ElegantDice/Symbols/ThreeLines/Line{i+1}/Material/Shader')
        line_shader.CreateIdAttr("UsdPreviewSurface")
        
        # Gradient from pink to light purple
        gradient_pos = float(i) / 2.0  # 0 to 1 across the three lines
        r = 0.9 - (0.3 * gradient_pos)
        g = 0.6 - (0.3 * gradient_pos)
        b = 0.9
        
        line_shader.CreateInput("diffuseColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(r, g, b))
        line_shader.CreateInput("emissiveColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(r*0.3, g*0.2, b*0.4))
        line_shader.CreateInput("metallic", Sdf.ValueTypeNames.Float).Set(0.3)
        line_shader.CreateInput("roughness", Sdf.ValueTypeNames.Float).Set(0.2)
        
        line_material.CreateSurfaceOutput().ConnectToSource(line_shader, "surface")
        UsdShade.MaterialBindingAPI(line).Bind(line_material)
    
    # Face 2: Circular glow (opposite face)
    circle = UsdGeom.Mesh.Define(stage, "/ElegantDice/Symbols/Circle")
    
    # Create a circular disc
    radius = 0.7
    segments = 32
    circle_points = []
    
    # Center point
    circle_points.append(Gf.Vec3f(0, 0, -(half_size + offset)))
    
    # Outer points
    for i in range(segments):
        angle = 2.0 * np.pi * i / segments
        x = radius * np.cos(angle)
        y = radius * np.sin(angle)
        circle_points.append(Gf.Vec3f(x, y, -(half_size + offset)))
    
    circle.CreatePointsAttr(circle_points)
    
    # Define faces (triangles from center to edge)
    face_vertex_counts = [3] * segments
    
    # Define vertex indices for each triangular face
    face_vertex_indices = []
    for i in range(segments):
        face_vertex_indices.extend([0, i+1, 1+((i+1) % segments)])
    
    circle.CreateFaceVertexCountsAttr(face_vertex_counts)
    circle.CreateFaceVertexIndicesAttr(face_vertex_indices)
    
    # Create and bind material
    circle_material = UsdShade.Material.Define(stage, '/ElegantDice/Symbols/Circle/Material')
    circle_shader = UsdShade.Shader.Define(stage, '/ElegantDice/Symbols/Circle/Material/Shader')
    circle_shader.CreateIdAttr("UsdPreviewSurface")
    
    # Radial gradient from center to edge
    circle_shader.CreateInput("diffuseColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(0.7, 0.4, 0.9))
    circle_shader.CreateInput("emissiveColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(0.3, 0.1, 0.5))
    circle_shader.CreateInput("metallic", Sdf.ValueTypeNames.Float).Set(0.5)
    circle_shader.CreateInput("roughness", Sdf.ValueTypeNames.Float).Set(0.3)
    
    circle_material.CreateSurfaceOutput().ConnectToSource(circle_shader, "surface")
    UsdShade.MaterialBindingAPI(circle).Bind(circle_material)
    
    # Face 3: Diamond pattern
    diamond = UsdGeom.Mesh.Define(stage, "/ElegantDice/Symbols/Diamond")
    
    # Create a diamond shape
    diamond_size = 0.8
    diamond_points = [
        Gf.Vec3f(0, diamond_size, -(half_size + offset)),             # top
        Gf.Vec3f(diamond_size, 0, -(half_size + offset)),             # right
        Gf.Vec3f(0, -diamond_size, -(half_size + offset)),            # bottom
        Gf.Vec3f(-diamond_size, 0, -(half_size + offset)),            # left
        Gf.Vec3f(0, 0, -(half_size + offset) - diamond_size/4.0)      # center back (for 3D effect)
    ]
    
    diamond.CreatePointsAttr(diamond_points)
    
    # Define faces (triangles)
    face_vertex_counts = [3, 3, 3, 3, 3, 3, 3, 3]
    
    # Define vertex indices for each triangular face
    face_vertex_indices = [
        0, 1, 4,  # top-right-center
        1, 2, 4,  # right-bottom-center
        2, 3, 4,  # bottom-left-center
        3, 0, 4,  # left-top-center
        0, 3, 4,  # top-left-center
        3, 2, 4,  # left-bottom-center
        2, 1, 4,  # bottom-right-center
        1, 0, 4   # right-top-center
    ]
    
    diamond.CreateFaceVertexCountsAttr(face_vertex_counts)
    diamond.CreateFaceVertexIndicesAttr(face_vertex_indices)
    
    # Create and bind material
    diamond_material = UsdShade.Material.Define(stage, '/ElegantDice/Symbols/Diamond/Material')
    diamond_shader = UsdShade.Shader.Define(stage, '/ElegantDice/Symbols/Diamond/Material/Shader')
    diamond_shader.CreateIdAttr("UsdPreviewSurface")
    
    diamond_shader.CreateInput("diffuseColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(0.3, 0.1, 0.5))
    diamond_shader.CreateInput("emissiveColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(0.1, 0.05, 0.2))
    diamond_shader.CreateInput("metallic", Sdf.ValueTypeNames.Float).Set(0.9)
    diamond_shader.CreateInput("roughness", Sdf.ValueTypeNames.Float).Set(0.05)
    
    diamond_material.CreateSurfaceOutput().ConnectToSource(diamond_shader, "surface")
    UsdShade.MaterialBindingAPI(diamond).Bind(diamond_material)
    
    # Face 4: Elegant "P" for Professional/Premium
    # (For simplicity, we'll use a simplified P shape with rectangles)
    p_symbol = UsdGeom.Xform.Define(stage, "/ElegantDice/Symbols/PSymbol")
    
    # Vertical bar of the P
    p_bar = UsdGeom.Mesh.Define(stage, "/ElegantDice/Symbols/PSymbol/Bar")
    p_bar_width = 0.15
    p_bar_height = 0.8
    
    p_bar_points = [
        Gf.Vec3f(-p_bar_width, -p_bar_height/2, half_size + offset),
        Gf.Vec3f(0, -p_bar_height/2, half_size + offset),
        Gf.Vec3f(0, p_bar_height/2, half_size + offset),
        Gf.Vec3f(-p_bar_width, p_bar_height/2, half_size + offset),
        
        Gf.Vec3f(-p_bar_width, -p_bar_height/2, half_size + offset + 0.05),
        Gf.Vec3f(0, -p_bar_height/2, half_size + offset + 0.05),
        Gf.Vec3f(0, p_bar_height/2, half_size + offset + 0.05),
        Gf.Vec3f(-p_bar_width, p_bar_height/2, half_size + offset + 0.05),
    ]
    
    p_bar.CreatePointsAttr(p_bar_points)
    
    face_vertex_counts = [4, 4, 4, 4, 4, 4]
    face_vertex_indices = [
        0, 1, 2, 3,  # front
        4, 5, 6, 7,  # back
        0, 3, 7, 4,  # left
        1, 2, 6, 5,  # right
        0, 1, 5, 4,  # bottom
        3, 2, 6, 7   # top
    ]
    
    p_bar.CreateFaceVertexCountsAttr(face_vertex_counts)
    p_bar.CreateFaceVertexIndicesAttr(face_vertex_indices)
    
    # Rounded part of the P
    p_round = UsdGeom.Mesh.Define(stage, "/ElegantDice/Symbols/PSymbol/Round")
    
    radius = 0.3
    center_x = 0.15
    center_y = 0.1
    segments = 16
    start_angle = -np.pi/2
    end_angle = np.pi/2
    
    p_round_points = []
    
    # Points for the front face
    for i in range(segments + 1):
        t = i / segments
        angle = start_angle + t * (end_angle - start_angle)
        x = center_x + radius * np.cos(angle)
        y = center_y + radius * np.sin(angle)
        p_round_points.append(Gf.Vec3f(x, y, half_size + offset))
    
    # Connect to the vertical bar
    p_round_points.append(Gf.Vec3f(0, center_y - radius, half_size + offset))
    p_round_points.append(Gf.Vec3f(0, center_y + radius, half_size + offset))
    
    # Points for the back face (with depth)
    back_start = len(p_round_points)
    for i in range(segments + 1):
        t = i / segments
        angle = start_angle + t * (end_angle - start_angle)
        x = center_x + radius * np.cos(angle)
        y = center_y + radius * np.sin(angle)
        p_round_points.append(Gf.Vec3f(x, y, half_size + offset + 0.05))
    
    # Connect to the vertical bar (back)
    p_round_points.append(Gf.Vec3f(0, center_y - radius, half_size + offset + 0.05))
    p_round_points.append(Gf.Vec3f(0, center_y + radius, half_size + offset + 0.05))
    
    p_round.CreatePointsAttr(p_round_points)
    
    # Define faces for the rounded part
    face_vertex_counts = []
    face_vertex_indices = []
    
    # Front face (as a triangle fan)
    for i in range(segments):
        face_vertex_counts.append(3)
        face_vertex_indices.extend([segments+1, i, i+1])
    
    # Back face (as a triangle fan)
    for i in range(segments):
        face_vertex_counts.append(3)
        face_vertex_indices.extend([back_start + segments+1, back_start + i + 1, back_start + i])
    
    # Side faces
    for i in range(segments):
        face_vertex_counts.append(4)
        face_vertex_indices.extend([
            i, i+1, 
            back_start + i+1, back_start + i
        ])
    
    # Connect to bar
    face_vertex_counts.append(4)
    face_vertex_indices.extend([segments, segments+1, back_start + segments+1, back_start + segments])
    
    p_round.CreateFaceVertexCountsAttr(face_vertex_counts)
    p_round.CreateFaceVertexIndicesAttr(face_vertex_indices)
    
    # Create and bind material for P symbol
    p_material = UsdShade.Material.Define(stage, '/ElegantDice/Symbols/PSymbol/Material')
    p_shader = UsdShade.Shader.Define(stage, '/ElegantDice/Symbols/PSymbol/Material/Shader')
    p_shader.CreateIdAttr("UsdPreviewSurface")
    
    p_shader.CreateInput("diffuseColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(0.85, 0.75, 0.95))
    p_shader.CreateInput("emissiveColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(0.3, 0.2, 0.4))
    p_shader.CreateInput("metallic", Sdf.ValueTypeNames.Float).Set(0.7)
    p_shader.CreateInput("roughness", Sdf.ValueTypeNames.Float).Set(0.15)
    
    p_material.CreateSurfaceOutput().ConnectToSource(p_shader, "surface")
    UsdShade.MaterialBindingAPI(p_bar).Bind(p_material)
    UsdShade.MaterialBindingAPI(p_round).Bind(p_material)
    
    # Face 5: Crown symbol (for top face)
    crown = UsdGeom.Mesh.Define(stage, "/ElegantDice/Symbols/Crown")
    
    # Create a simplified crown shape
    crown_points = [
        # Base points
        Gf.Vec3f(-0.5, -0.3, half_size + offset),
        Gf.Vec3f(0.5, -0.3, half_size + offset),
        Gf.Vec3f(0.5, -0.1, half_size + offset),
        Gf.Vec3f(-0.5, -0.1, half_size + offset),
        
        # Points with height
        Gf.Vec3f(-0.5, -0.3, half_size + offset + 0.05),
        Gf.Vec3f(0.5, -0.3, half_size + offset + 0.05),
        Gf.Vec3f(0.5, -0.1, half_size + offset + 0.05),
        Gf.Vec3f(-0.5, -0.1, half_size + offset + 0.05),
        
        # Crown peaks
        Gf.Vec3f(-0.4, 0.3, half_size + offset),
        Gf.Vec3f(-0.2, 0.4, half_size + offset),
        Gf.Vec3f(0.0, 0.3, half_size + offset),
        Gf.Vec3f(0.2, 0.4, half_size + offset),
        Gf.Vec3f(0.4, 0.3, half_size + offset),
        
        Gf.Vec3f(-0.4, 0.3, half_size + offset + 0.05),
        Gf.Vec3f(-0.2, 0.4, half_size + offset + 0.05),
        Gf.Vec3f(0.0, 0.3, half_size + offset + 0.05),
        Gf.Vec3f(0.2, 0.4, half_size + offset + 0.05),
        Gf.Vec3f(0.4, 0.3, half_size + offset + 0.05),
    ]
    
    crown.CreatePointsAttr(crown_points)
    
    # Define faces for the crown
    face_vertex_counts = []
    face_vertex_indices = []
    
    # Base rectangle
    face_vertex_counts.append(4)
    face_vertex_indices.extend([0, 1, 2, 3])  # front
    
    face_vertex_counts.append(4)
    face_vertex_indices.extend([4, 5, 6, 7])  # back
    
    face_vertex_counts.append(4)
    face_vertex_indices.extend([0, 3, 7, 4])  # left
    
    face_vertex_counts.append(4)
    face_vertex_indices.extend([1, 2, 6, 5])  # right
    
    face_vertex_counts.append(4)
    face_vertex_indices.extend([0, 1, 5, 4])  # bottom
    
    # Crown peaks front
    for i in range(4):
        face_vertex_counts.append(3)
        face_vertex_indices.extend([3, 8+i, 8+i+1])
    
    # Crown peaks back
    for i in range(4):
        face_vertex_counts.append(3)
        face_vertex_indices.extend([7, 13+i+1, 13+i])
    
    # Crown peak sides
    for i in range(5):
        face_vertex_counts.append(4)
        face_vertex_indices.extend([8+i, 13+i, 13+i+1 if i<4 else 13, 8+i+1 if i<4 else 8])
    
    crown.CreateFaceVertexCountsAttr(face_vertex_counts)
    crown.CreateFaceVertexIndicesAttr(face_vertex_indices)
    
    # Create and bind material for crown
    crown_material = UsdShade.Material.Define(stage, '/ElegantDice/Symbols/Crown/Material')
    crown_shader = UsdShade.Shader.Define(stage, '/ElegantDice/Symbols/Crown/Material/Shader')
    crown_shader.CreateIdAttr("UsdPreviewSurface")
    
    crown_shader.CreateInput("diffuseColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(0.9, 0.8, 0.2))  # Gold
    crown_shader.CreateInput("emissiveColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(0.2, 0.1, 0.05))
    crown_shader.CreateInput("metallic", Sdf.ValueTypeNames.Float).Set(1.0)
    crown_shader.CreateInput("roughness", Sdf.ValueTypeNames.Float).Set(0.1)
    
    crown_material.CreateSurfaceOutput().ConnectToSource(crown_shader, "surface")
    UsdShade.MaterialBindingAPI(crown).Bind(crown_material)
    
    # Face 6: Spiral pattern
    spiral = UsdGeom.Mesh.Define(stage, "/ElegantDice/Symbols/Spiral")
    
    # Create a spiral shape
    center_x, center_y = 0, 0
    spiral_points = []
    segments = 120
    max_radius = 0.7
    
    # Create the spiral path
    for i in range(segments + 1):
        t = i / segments
        radius = t * max_radius
        angle = t * 6 * np.pi
        x = center_x + radius * np.cos(angle)
        y = center_y + radius * np.sin(angle)
        spiral_points.append(Gf.Vec3f(x, y, -(half_size + offset)))
    
    # Create points with thickness (duplicate points with offset)
    spiral_thickness = 0.05
    spiral_points_with_thickness = []
    spiral_face_vertex_counts = []
    spiral_face_vertex_indices = []
    
    # Create a tube along the spiral
    for i in range(segments):
        p1 = spiral_points[i]
        p2 = spiral_points[i+1]
        
        # Direction vector
        dx = p2[0] - p1[0]
        dy = p2[1] - p1[1]
        length = np.sqrt(dx*dx + dy*dy)
        
        # Normalized normal vector (perpendicular to direction)
        nx = -dy / length
        ny = dx / length
        
        # Add two points for current segment (sides of the tube)
        p1_left = Gf.Vec3f(p1[0] + nx * spiral_thickness, p1[1] + ny * spiral_thickness, p1[2])
        p1_right = Gf.Vec3f(p1[0] - nx * spiral_thickness, p1[1] - ny * spiral_thickness, p1[2])
        
        spiral_points_with_thickness.append(p1_left)
        spiral_points_with_thickness.append(p1_right)
        
        # For the last point, add the end points
        if i == segments - 1:
            p2_left = Gf.Vec3f(p2[0] + nx * spiral_thickness, p2[1] + ny * spiral_thickness, p2[2])
            p2_right = Gf.Vec3f(p2[0] - nx * spiral_thickness, p2[1] - ny * spiral_thickness, p2[2])
            
            spiral_points_with_thickness.append(p2_left)
            spiral_points_with_thickness.append(p2_right)
        
        # If not the first segment, create face connecting to previous segment
        if i > 0:
            idx = i * 2
            spiral_face_vertex_counts.append(4)
            spiral_face_vertex_indices.extend([idx-2, idx, idx+1, idx-1])
        
        # For the first and last segments, create end caps
        if i == 0:
            spiral_face_vertex_counts.append(2)
            spiral_face_vertex_indices.extend([0, 1])
        
        if i == segments - 1:
            spiral_face_vertex_counts.append(2)
            spiral_face_vertex_indices.extend([idx+2, idx+3])
    
    spiral.CreatePointsAttr(spiral_points_with_thickness)
    spiral.CreateFaceVertexCountsAttr(spiral_face_vertex_counts)
    spiral.CreateFaceVertexIndicesAttr(spiral_face_vertex_indices)
    
    # Create and bind material for spiral
    spiral_material = UsdShade.Material.Define(stage, '/ElegantDice/Symbols/Spiral/Material')
    spiral_shader = UsdShade.Shader.Define(stage, '/ElegantDice/Symbols/Spiral/Material/Shader')
    spiral_shader.CreateIdAttr("UsdPreviewSurface")
    
    # Gradient from purple to pink
    spiral_shader.CreateInput("diffuseColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(0.6, 0.3, 0.8))
    spiral_shader.CreateInput("emissiveColor", Sdf.ValueTypeNames.Color3f).Set(Gf.Vec3f(0.2, 0.1, 0.3))
    spiral_shader.CreateInput("metallic", Sdf.ValueTypeNames.Float).Set(0.6)
    spiral_shader.CreateInput("roughness", Sdf.ValueTypeNames.Float).Set(0.2)
    
    spiral_material.CreateSurfaceOutput().ConnectToSource(spiral_shader, "surface")
    UsdShade.MaterialBindingAPI(spiral).Bind(spiral_material)
    
    # Create animation for the dice (optional)
    # Add rotation animation
    rotation_attr = root_prim.AddRotateXYZOp(UsdGeom.XformOp.PrecisionDouble, "rotation")
    
    # Set keyframes for rotation
    rotation_attr.Set(Gf.Vec3d(0, 0, 0), time=1)
    rotation_attr.Set(Gf.Vec3d(360, 360, 360), time=24)

if __name__ == "__main__":
    create_elegant_dice()