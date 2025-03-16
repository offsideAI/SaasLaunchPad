import bpy
import math
import mathutils
from math import radians

# Clear existing objects
bpy.ops.object.select_all(action='SELECT')
bpy.ops.object.delete()

# Create a new material with gradient
def create_gradient_material(name, color1, color2, metallic=0.8, roughness=0.1):
    material = bpy.data.materials.new(name=name)
    material.use_nodes = True
    nodes = material.node_tree.nodes
    links = material.node_tree.links
    
    # Clear default nodes
    for node in nodes:
        nodes.remove(node)
    
    # Create nodes
    output = nodes.new(type='ShaderNodeOutputMaterial')
    principled = nodes.new(type='ShaderNodeBsdfPrincipled')
    gradient = nodes.new(type='ShaderNodeTexGradient')
    mapping = nodes.new(type='ShaderNodeMapping')
    tex_coord = nodes.new(type='ShaderNodeTexCoord')
    color_ramp = nodes.new(type='ShaderNodeValToRGB')
    
    # Set up color ramp
    color_ramp.color_ramp.elements[0].color = color1
    color_ramp.color_ramp.elements[1].color = color2
    
    # Set material properties
    principled.inputs['Metallic'].default_value = metallic
    principled.inputs['Roughness'].default_value = roughness
    principled.inputs['Specular'].default_value = 0.7
    
    # Link nodes
    links.new(tex_coord.outputs['Generated'], mapping.inputs['Vector'])
    links.new(mapping.outputs['Vector'], gradient.inputs['Vector'])
    links.new(gradient.outputs['Color'], color_ramp.inputs['Fac'])
    links.new(color_ramp.outputs['Color'], principled.inputs['Base Color'])
    links.new(principled.outputs['BSDF'], output.inputs['Surface'])
    
    # Position nodes
    output.location = (300, 0)
    principled.location = (0, 0)
    color_ramp.location = (-300, 0)
    gradient.location = (-500, 0)
    mapping.location = (-700, 0)
    tex_coord.location = (-900, 0)
    
    return material

# Create main dice material
dice_material = create_gradient_material(
    "ElegantDice",
    (0.05, 0.02, 0.2, 1.0),  # Deep navy
    (0.4, 0.15, 0.5, 1.0)    # Rich purple
)

# Create a cube for the dice
bpy.ops.mesh.primitive_cube_add(size=2.0, location=(0, 0, 0))
dice = bpy.context.active_object
dice.name = "ElegantDice"

# Apply material to dice
if dice.data.materials:
    dice.data.materials[0] = dice_material
else:
    dice.data.materials.append(dice_material)

# Bevel the edges for a more elegant look
bpy.ops.object.modifier_add(type='BEVEL')
bevel_mod = dice.modifiers["Bevel"]
bevel_mod.width = 0.05
bevel_mod.segments = 3
bevel_mod.limit_method = 'ANGLE'
bevel_mod.angle_limit = radians(40)

# Create a three lines symbol (for face 1)
def create_three_lines(location, rotation, material=None):
    lines_parent = bpy.data.objects.new("ThreeLines", None)
    bpy.context.collection.objects.link(lines_parent)
    lines_parent.empty_display_size = 0.1
    lines_parent.location = location
    lines_parent.rotation_euler = rotation
    
    for i in range(3):
        y_offset = 0.3 * (i - 1)
        
        bpy.ops.mesh.primitive_cube_add(
            size=1.0, 
            location=(0, y_offset, 0)
        )
        line = bpy.context.active_object
        line.name = f"Line_{i+1}"
        line.parent = lines_parent
        
        # Scale the line
        line.scale = (0.6, 0.1, 0.02)
        
        # Create gradient material from pink to purple
        gradient_pos = i / 2.0
        r = 0.9 - (0.3 * gradient_pos)
        g = 0.6 - (0.3 * gradient_pos)
        b = 0.9
        
        line_material = create_gradient_material(
            f"Line_Material_{i+1}",
            (r, g, b, 1.0),
            (r*0.7, g*0.7, b*0.9, 1.0),
            metallic=0.5,
            roughness=0.2
        )
        
        # Apply material
        if line.data.materials:
            line.data.materials[0] = line_material
        else:
            line.data.materials.append(line_material)
    
    return lines_parent

# Create a circular symbol (for face 2)
def create_circle(location, rotation, material=None):
    bpy.ops.mesh.primitive_circle_add(
        vertices=32,
        radius=0.7,
        fill_type='NGON',
        location=location
    )
    circle = bpy.context.active_object
    circle.name = "CircleSymbol"
    circle.rotation_euler = rotation
    
    # Extrude to give it some depth
    bpy.ops.object.mode_set(mode='EDIT')
    bpy.ops.mesh.extrude_region_move(
        TRANSFORM_OT_translate=(0, 0, 0.02)
    )
    bpy.ops.object.mode_set(mode='OBJECT')
    
    # Create a radial gradient material
    circle_material = create_gradient_material(
        "Circle_Material",
        (0.7, 0.4, 0.9, 1.0),
        (0.4, 0.2, 0.7, 1.0),
        metallic=0.5,
        roughness=0.3
    )
    
    # Apply material
    if circle.data.materials:
        circle.data.materials[0] = circle_material
    else:
        circle.data.materials.append(circle_material)
    
    return circle

# Create a diamond symbol (for face 3)
def create_diamond(location, rotation, material=None):
    verts = [
        (0, 0.8, 0),          # top
        (0.8, 0, 0),          # right
        (0, -0.8, 0),         # bottom
        (-0.8, 0, 0),         # left
        (0, 0, -0.2)          # center back (for 3D effect)
    ]
    
    faces = [
        (0, 1, 4),            # top-right-center
        (1, 2, 4),            # right-bottom-center
        (2, 3, 4),            # bottom-left-center
        (3, 0, 4),            # left-top-center
    ]
    
    mesh = bpy.data.meshes.new("DiamondMesh")
    mesh.from_pydata(verts, [], faces)
    mesh.update()
    
    diamond = bpy.data.objects.new("DiamondSymbol", mesh)
    bpy.context.collection.objects.link(diamond)
    diamond.location = location
    diamond.rotation_euler = rotation
    
    # Create a metallic purple material
    diamond_material = create_gradient_material(
        "Diamond_Material",
        (0.3, 0.1, 0.5, 1.0),
        (0.2, 0.05, 0.3, 1.0),
        metallic=0.9,
        roughness=0.05
    )
    
    # Apply material
    if diamond.data.materials:
        diamond.data.materials[0] = diamond_material
    else:
        diamond.data.materials.append(diamond_material)
    
    return diamond

# Create a "P" symbol (for face 4)
def create_p_symbol(location, rotation, material=None):
    # Create a mesh for the P symbol
    verts = []
    edges = []
    faces = []
    
    # Vertical bar
    bar_width = 0.15
    bar_height = 0.8
    bar_depth = 0.05
    
    # Add vertices for the bar
    verts.extend([
        (-bar_width/2, -bar_height/2, 0),
        (bar_width/2, -bar_height/2, 0),
        (bar_width/2, bar_height/2, 0),
        (-bar_width/2, bar_height/2, 0),
        
        (-bar_width/2, -bar_height/2, bar_depth),
        (bar_width/2, -bar_height/2, bar_depth),
        (bar_width/2, bar_height/2, bar_depth),
        (-bar_width/2, bar_height/2, bar_depth),
    ])
    
    # Add faces for the bar
    faces.extend([
        (0, 1, 2, 3),  # front
        (4, 5, 6, 7),  # back
        (0, 3, 7, 4),  # left
        (1, 2, 6, 5),  # right
        (0, 1, 5, 4),  # bottom
        (3, 2, 6, 7)   # top
    ])
    
    # Create the bar mesh
    p_mesh = bpy.data.meshes.new("PSymbolMesh")
    p_mesh.from_pydata(verts, edges, faces)
    p_mesh.update()
    
    p_obj = bpy.data.objects.new("PSymbol", p_mesh)
    bpy.context.collection.objects.link(p_obj)
    p_obj.location = location
    p_obj.rotation_euler = rotation
    
    # Add a round part to the P
    bpy.ops.mesh.primitive_cylinder_add(
        vertices=16,
        radius=0.3,
        depth=bar_depth,
        end_fill_type='NGON',
        location=(location[0] + 0.15, location[1] + 0.1, location[2] + bar_depth/2)
    )
    p_round = bpy.context.active_object
    p_round.rotation_euler = rotation
    p_round.scale.y = 0.5
    
    # Apply boolean operation to cut the cylinder in half
    bpy.ops.object.modifier_add(type='BOOLEAN')
    bool_mod = p_round.modifiers["Boolean"]
    
    # Create a cube to cut the cylinder
    bpy.ops.mesh.primitive_cube_add(
        size=1.0,
        location=(location[0] + 0.15, location[1] - 0.15, location[2])
    )
    cutter = bpy.context.active_object
    cutter.scale = (1, 1, 1)
    
    # Set up boolean operation
    bool_mod.operation = 'DIFFERENCE'
    bool_mod.object = cutter
    bpy.ops.object.modifier_apply(modifier="Boolean")
    
    # Delete the cutter
    bpy.data.objects.remove(cutter)
    
    # Create elegant light purple material
    p_material = create_gradient_material(
        "P_Material",
        (0.85, 0.75, 0.95, 1.0),
        (0.6, 0.5, 0.8, 1.0),
        metallic=0.7,
        roughness=0.15
    )
    
    # Apply material to both parts
    if p_obj.data.materials:
        p_obj.data.materials[0] = p_material
    else:
        p_obj.data.materials.append(p_material)
    
    if p_round.data.materials:
        p_round.data.materials[0] = p_material
    else:
        p_round.data.materials.append(p_material)
    
    # Group the parts
    p_round.parent = p_obj
    
    return p_obj

# Create a crown symbol (for face 5)
def create_crown(location, rotation, material=None):
    # Create base vertices for the crown
    verts = [
        # Base points
        (-0.5, -0.3, 0),
        (0.5, -0.3, 0),
        (0.5, -0.1, 0),
        (-0.5, -0.1, 0),
        
        # Back points
        (-0.5, -0.3, 0.05),
        (0.5, -0.3, 0.05),
        (0.5, -0.1, 0.05),
        (-0.5, -0.1, 0.05),
        
        # Crown peaks front
        (-0.4, 0.3, 0),
        (-0.2, 0.4, 0),
        (0.0, 0.3, 0),
        (0.2, 0.4, 0),
        (0.4, 0.3, 0),
        
        # Crown peaks back
        (-0.4, 0.3, 0.05),
        (-0.2, 0.4, 0.05),
        (0.0, 0.3, 0.05),
        (0.2, 0.4, 0.05),
        (0.4, 0.3, 0.05),
    ]
    
    # Define faces
    faces = [
        # Base rectangle
        (0, 1, 2, 3),  # front
        (4, 5, 6, 7),  # back
        (0, 3, 7, 4),  # left
        (1, 2, 6, 5),  # right
        (0, 1, 5, 4),  # bottom
        
        # Crown peaks front
        (3, 8, 9),
        (3, 9, 10),
        (3, 10, 11),
        (3, 11, 12),
        (3, 12, 2),
        
        # Crown peaks back
        (7, 13, 14),
        (7, 14, 15),
        (7, 15, 16),
        (7, 16, 17),
        (7, 17, 6),
        
        # Connect front to back
        (8, 13, 14, 9),
        (9, 14, 15, 10),
        (10, 15, 16, 11),
        (11, 16, 17, 12),
        (12, 17, 6, 2),
    ]
    
    # Create the mesh
    crown_mesh = bpy.data.meshes.new("CrownMesh")
    crown_mesh.from_pydata(verts, [], faces)
    crown_mesh.update()
    
    crown = bpy.data.objects.new("CrownSymbol", crown_mesh)
    bpy.context.collection.objects.link(crown)
    crown.location = location
    crown.rotation_euler = rotation
    
    # Create gold material
    crown_material = create_gradient_material(
        "Crown_Material",
        (0.9, 0.8, 0.2, 1.0),  # Gold
        (0.8, 0.6, 0.1, 1.0),  # Darker gold
        metallic=1.0,
        roughness=0.1
    )
    
    # Apply material
    if crown.data.materials:
        crown.data.materials[0] = crown_material
    else:
        crown.data.materials.append(crown_material)
    
    return crown

# Create a spiral symbol (for face 6)
def create_spiral(location, rotation, material=None):
    # Create a spiral curve
    bpy.ops.curve.primitive_bezier_circle_add(radius=0.02, location=location)
    spiral = bpy.context.active_object
    spiral.name = "SpiralSymbol"
    spiral.rotation_euler = rotation
    
    # Convert to a path
    spiral.data.dimensions = '3D'
    spiral.data.resolution_u = 64
    spiral.data.bevel_depth = 0.02
    spiral.data.bevel_resolution = 3
    
    # Add points to create a spiral
    spiral.data.splines[0].bezier_points.add(15)  # Add more points
    
    # Set the points to create a spiral shape
    center_x, center_y, center_z = location
    points = spiral.data.splines[0].bezier_points
    
    for i in range(16):
        t = i / 15.0
        radius = 0.7 * t
        angle = t * 6 * math.pi
        
        x = center_x + radius * math.cos(angle)
        y = center_y + radius * math.sin(angle)
        z = center_z
        
        points[i].co = (x, y, z)
        points[i].handle_left_type = 'AUTO'
        points[i].handle_right_type = 'AUTO'
    
    # Create gradient material for spiral
    spiral_material = create_gradient_material(
        "Spiral_Material",
        (0.6, 0.3, 0.8, 1.0),
        (0.8, 0.4, 0.6, 1.0),
        metallic=0.6,
        roughness=0.2
    )
    
    # Apply material
    if spiral.data.materials:
        spiral.data.materials[0] = spiral_material
    else:
        spiral.data.materials.append(spiral_material)
    
    return spiral

# Create symbols for each face
create_three_lines(
    location=(0, 0, 1.02),
    rotation=(0, 0, 0)
)

create_circle(
    location=(0, 0, -1.02),
    rotation=(0, 0, 0)
)

create_diamond(
    location=(0, 1.02, 0),
    rotation=(math.pi/2, 0, 0)
)

create_p_symbol(
    location=(0, -1.02, 0),
    rotation=(math.pi/2, 0, 0)
)

create_crown(
    location=(1.02, 0, 0),
    rotation=(0, math.pi/2, 0)
)

create_spiral(
    location=(-1.02, 0, 0),
    rotation=(0, -math.pi/2, 0)
)

# Setup rendering
scene = bpy.context.scene
scene.render.engine = 'CYCLES'
scene.cycles.device = 'GPU'
scene.cycles.samples = 128

# Add lighting
bpy.ops.object.light_add(type='SUN', location=(5, 5, 10))
sun = bpy.context.active_object
sun.data.energy = 2.0

# Add environment lighting with gradient
world = bpy.context.scene.world
world.use_nodes = True
world_nodes = world.node_tree.nodes
world_links = world.node_tree.links

# Clear existing nodes
for node in world_nodes:
    world_nodes.remove(node)

# Create new nodes
world_output = world_nodes.new(type='ShaderNodeOutputWorld')
background = world_nodes.new(type='ShaderNodeBackground')
gradient = world_nodes.new(type='ShaderNodeTexGradient')
mapping = world_nodes.new(type='ShaderNodeMapping')
tex_coord = world_nodes.new(type='ShaderNodeTexCoord')
color_ramp = world_nodes.new(type='ShaderNodeValToRGB')

# Set up color ramp for environment
color_ramp.color_ramp.elements[0].color = (0.05, 0.02, 0.1, 1.0)  # Dark navy
color_ramp.color_ramp.elements[1].color = (0.2, 0.1, 0.3, 1.0)   # Purple

# Connect nodes
world_links.new(tex_coord.outputs['Generated'], mapping.inputs['Vector'])
world_links.new(mapping.outputs['Vector'], gradient.inputs['Vector'])
world_links.new(gradient.outputs['Color'], color_ramp.inputs['Fac'])
world_links.new(color_ramp.outputs['Color'], background.inputs['Color'])
world_links.new(background.outputs['Background'], world_output.inputs['Surface'])

# Position world nodes
world_output.location = (300, 0)
background.location = (100, 0)
color_ramp.location = (-100, 0)
gradient.location = (-300, 0)
mapping.location = (-500, 0)
tex_coord.location = (-700, 0)

# Add camera and set its position
bpy.ops.object.camera_add(location=(4, -4, 3))
camera = bpy.context.active_object
camera.rotation_euler = (math.radians(60), 0, math.radians(45))

# Set the camera as active
scene.camera = camera

# Set animation
scene.frame_start = 1
scene.frame_end = 72

# Add rotation animation to the dice
dice.keyframe_insert(data_path="rotation_euler", frame=1)
dice.rotation_euler = (math.radians(360), math.radians(360), math.radians(360))
dice.keyframe_insert(data_path="rotation_euler", frame=72)

print("Elegant dice created successfully!")