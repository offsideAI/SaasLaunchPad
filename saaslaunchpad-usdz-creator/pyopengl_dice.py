import sys
import math
import numpy as np
from PIL import Image, ImageDraw
import pygame
from pygame.locals import *
from OpenGL.GL import *
from OpenGL.GLU import *
from OpenGL.GLUT import *

# Install required packages with:
# pip install numpy pillow pygame PyOpenGL PyOpenGL_accelerate

class ElegantDice:
    def __init__(self):
        # Initialize pygame and OpenGL
        pygame.init()
        display = (800, 600)
        pygame.display.set_mode(display, DOUBLEBUF | OPENGL)
        pygame.display.set_caption('Elegant 3D Dice')
        
        # Set up the OpenGL environment
        glEnable(GL_DEPTH_TEST)
        
        # Set background color (dark blue)
        glClearColor(0.05, 0.05, 0.2, 1.0)
        
        # Set up the perspective
        gluPerspective(45, (display[0] / display[1]), 0.1, 50.0)
        glTranslatef(0.0, 0.0, -5)
        
        # Create and bind textures
        self.textures = self.create_textures()
        
        # Animation variables
        self.rotation = [0, 0, 0]
        self.auto_rotate = True
        self.clock = pygame.time.Clock()
    
    def create_textures(self):
        textures = []
        
        # Generate texture images
        textures.append(self.create_three_lines_texture())   # Face 1
        textures.append(self.create_circle_texture())        # Face 2
        textures.append(self.create_diamond_texture())       # Face 3
        textures.append(self.create_p_symbol_texture())      # Face 4
        textures.append(self.create_crown_texture())         # Face 5
        textures.append(self.create_spiral_texture())        # Face 6
        
        return textures
    
    def create_gradient_background(self, size=512, color1=(26, 35, 126), color2=(123, 31, 162)):
        # Create a gradient background image
        img = Image.new('RGBA', (size, size), color=(0, 0, 0, 0))
        draw = ImageDraw.Draw(img)
        
        for y in range(size):
            r = int(color1[0] + (color2[0] - color1[0]) * (y / size))
            g = int(color1[1] + (color2[1] - color1[1]) * (y / size))
            b = int(color1[2] + (color2[2] - color1[2]) * (y / size))
            draw.line([(0, y), (size, y)], fill=(r, g, b))
        
        return img
    
    def create_three_lines_texture(self):
        # Create texture with three horizontal lines
        img = self.create_gradient_background(color1=(26, 35, 126), color2=(123, 31, 162))
        draw = ImageDraw.Draw(img)
        
        width, height = img.size
        line_width = int(width * 0.6)
        line_height = int(height * 0.08)
        line_spacing = int(height * 0.2)
        
        for i in range(3):
            y_pos = int(height * 0.5) - line_spacing + (i * line_spacing)
            x_start = int((width - line_width) / 2)
            
            # Create gradient for the line
            for x in range(line_width):
                r = int(216 - (50 * (x / line_width)))
                g = int(161 - (60 * (x / line_width)))
                b = int(240 - (40 * (x / line_width)))
                
                draw.line(
                    [(x_start + x, y_pos), (x_start + x, y_pos + line_height)],
                    fill=(r, g, b)
                )
        
        # Convert to format suitable for OpenGL
        raw_data = img.tobytes("raw", "RGBA", 0, -1)
        texture_id = glGenTextures(1)
        
        glBindTexture(GL_TEXTURE_2D, texture_id)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.size[0], img.size[1], 0, GL_RGBA, GL_UNSIGNED_BYTE, raw_data)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        
        return texture_id
    
    def create_circle_texture(self):
        # Create texture with a circular gradient
        img = self.create_gradient_background(color1=(52, 39, 91), color2=(123, 31, 162))
        draw = ImageDraw.Draw(img)
        
        width, height = img.size
        radius = int(width * 0.35)
        center_x, center_y = width // 2, height // 2
        
        # Draw gradient circle
        for r in range(radius, 0, -1):
            # Gradient from outer to inner
            ratio = r / radius
            red = int(176 - (50 * (1 - ratio)))
            green = int(101 - (60 * (1 - ratio)))
            blue = int(170 - (30 * (1 - ratio)))
            
            draw.ellipse(
                [(center_x - r, center_y - r), (center_x + r, center_y + r)],
                outline=(red, green, blue)
            )
        
        # Fill the center
        draw.ellipse(
            [(center_x - 5, center_y - 5), (center_x + 5, center_y + 5)],
            fill=(150, 80, 150)
        )
        
        # Convert to format suitable for OpenGL
        raw_data = img.tobytes("raw", "RGBA", 0, -1)
        texture_id = glGenTextures(1)
        
        glBindTexture(GL_TEXTURE_2D, texture_id)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.size[0], img.size[1], 0, GL_RGBA, GL_UNSIGNED_BYTE, raw_data)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        
        return texture_id
    
    def create_diamond_texture(self):
        # Create texture with a diamond shape
        img = self.create_gradient_background(color1=(26, 35, 126), color2=(123, 31, 162))
        draw = ImageDraw.Draw(img)
        
        width, height = img.size
        diamond_size = int(width * 0.4)
        center_x, center_y = width // 2, height // 2
        
        # Points for the diamond
        points = [
            (center_x, center_y - diamond_size),  # top
            (center_x + diamond_size, center_y),  # right
            (center_x, center_y + diamond_size),  # bottom
            (center_x - diamond_size, center_y),  # left
        ]
        
        # Draw gradient diamond
        for i in range(diamond_size, 0, -2):
            ratio = i / diamond_size
            
            # Scale the points
            scaled_points = [
                (center_x, center_y - i),
                (center_x + i, center_y),
                (center_x, center_y + i),
                (center_x - i, center_y),
            ]
            
            red = int(74 - (20 * (1 - ratio)))
            green = int(20 - (10 * (1 - ratio)))
            blue = int(140 - (40 * (1 - ratio)))
            
            draw.polygon(scaled_points, outline=(red, green, blue))
            
            if i % 5 == 0:  # Only fill every few pixels to create a gradient effect
                draw.polygon(scaled_points, fill=(red, green, blue))
        
        # Convert to format suitable for OpenGL
        raw_data = img.tobytes("raw", "RGBA", 0, -1)
        texture_id = glGenTextures(1)
        
        glBindTexture(GL_TEXTURE_2D, texture_id)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.size[0], img.size[1], 0, GL_RGBA, GL_UNSIGNED_BYTE, raw_data)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        
        return texture_id
    
    def create_p_symbol_texture(self):
        # Create texture with the letter "P"
        img = self.create_gradient_background(color1=(26, 35, 126), color2=(123, 31, 162))
        draw = ImageDraw.Draw(img)
        
        width, height = img.size
        p_height = int(height * 0.6)
        p_width = int(p_height * 0.4)
        round_size = int(p_width * 1.2)
        
        # Starting position
        start_x = int(width * 0.3)
        start_y = int(height * 0.2)
        
        # Draw vertical bar
        for x in range(p_width):
            for y in range(p_height):
                # Gradient
                ratio = y / p_height
                red = int(216 - (20 * ratio))
                green = int(169 - (30 * ratio))
                blue = int(240 - (10 * ratio))
                
                draw.point((start_x + x, start_y + y), fill=(red, green, blue))
        
        # Draw rounded part
        for angle in range(0, 180):
            rad = math.radians(angle)
            x = start_x + p_width + round_size * math.cos(rad)
            y = start_y + round_size + round_size * math.sin(rad)
            
            # Gradient based on angle
            ratio = angle / 180
            red = int(216 - (20 * ratio))
            green = int(169 - (30 * ratio))
            blue = int(240 - (10 * ratio))
            
            draw.line([(x, y), (x+1, y+1)], fill=(red, green, blue), width=10)
        
        # Convert to format suitable for OpenGL
        raw_data = img.tobytes("raw", "RGBA", 0, -1)
        texture_id = glGenTextures(1)
        
        glBindTexture(GL_TEXTURE_2D, texture_id)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.size[0], img.size[1], 0, GL_RGBA, GL_UNSIGNED_BYTE, raw_data)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        
        return texture_id
    
    def create_crown_texture(self):
        # Create texture with a crown symbol
        img = self.create_gradient_background(color1=(26, 35, 126), color2=(123, 31, 162))
        draw = ImageDraw.Draw(img)
        
        width, height = img.size
        crown_width = int(width * 0.7)
        crown_height = int(height * 0.5)
        
        base_x = (width - crown_width) // 2
        base_y = height - int(height * 0.4)
        
        # Draw crown base
        points = [
            (base_x, base_y),
            (base_x + crown_width, base_y),
            (base_x + crown_width, base_y - int(crown_height * 0.3)),
            (base_x, base_y - int(crown_height * 0.3)),
        ]
        
        # Gold gradient for crown
        for y in range(base_y - int(crown_height * 0.3), base_y):
            ratio = (y - (base_y - int(crown_height * 0.3))) / int(crown_height * 0.3)
            red = int(255 - (55 * ratio))
            green = int(215 - (75 * ratio))
            blue = int(0 + (20 * ratio))
            
            draw.line(
                [(base_x, y), (base_x + crown_width, y)],
                fill=(red, green, blue)
            )
        
        # Draw crown points
        point_height = int(crown_height * 0.7)
        num_points = 5
        point_width = crown_width // (num_points * 2)
        
        for i in range(num_points):
            point_x = base_x + point_width + (i * point_width * 2)
            
            # Alternate point heights
            if i % 2 == 0:
                height_factor = 1.0
            else:
                height_factor = 0.7
                
            # Draw the point (triangle)
            points = [
                (point_x, base_y - int(crown_height * 0.3)),
                (point_x - point_width, base_y - int(crown_height * 0.3) - int(point_height * height_factor)),
                (point_x + point_width, base_y - int(crown_height * 0.3) - int(point_height * height_factor)),
            ]
            
            # Gold gradient for points
            for y in range(base_y - int(crown_height * 0.3) - int(point_height * height_factor), base_y - int(crown_height * 0.3)):
                ratio = (y - (base_y - int(crown_height * 0.3) - int(point_height * height_factor))) / int(point_height * height_factor)
                red = int(255 - (55 * ratio))
                green = int(215 - (75 * ratio))
                blue = int(0 + (30 * ratio))
                
                # Calculate x bounds for this y level
                progress = 1 - ratio
                x_width = point_width * progress
                
                draw.line(
                    [(point_x - x_width, y), (point_x + x_width, y)],
                    fill=(red, green, blue)
                )
        
        # Convert to format suitable for OpenGL
        raw_data = img.tobytes("raw", "RGBA", 0, -1)
        texture_id = glGenTextures(1)
        
        glBindTexture(GL_TEXTURE_2D, texture_id)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.size[0], img.size[1], 0, GL_RGBA, GL_UNSIGNED_BYTE, raw_data)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        
        return texture_id
    
    def create_spiral_texture(self):
        # Create texture with a spiral pattern
        img = self.create_gradient_background(color1=(26, 35, 126), color2=(123, 31, 162))
        draw = ImageDraw.Draw(img)
        
        width, height = img.size
        center_x, center_y = width // 2, height // 2
        max_radius = int(width * 0.4)
        
        # Draw spiral
        theta = 0
        while theta < 6 * 2 * np.pi:
            radius = (theta / (6 * 2 * np.pi)) * max_radius
            x = center_x + int(radius * np.cos(theta))
            y = center_y + int(radius * np.sin(theta))
            
            # Color gradient based on angle
            ratio = theta / (6 * 2 * np.pi)
            red = int(150 + (50 * ratio))
            green = int(80 + (30 * ratio))
            blue = int(200 - (20 * ratio))
            
            draw.ellipse([(x-5, y-5), (x+5, y+5)], fill=(red, green, blue))
            
            theta += 0.1
        
        # Convert to format suitable for OpenGL
        raw_data = img.tobytes("raw", "RGBA", 0, -1)
        texture_id = glGenTextures(1)
        
        glBindTexture(GL_TEXTURE_2D, texture_id)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, img.size[0], img.size[1], 0, GL_RGBA, GL_UNSIGNED_BYTE, raw_data)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        
        return texture_id
    
    def draw_dice(self):
        # Draw a cube with our textures
        vertices = [
            # Front face (z = 1.0)
            [-1.0, -1.0, 1.0],  # Bottom left
            [1.0, -1.0, 1.0],   # Bottom right
            [1.0, 1.0, 1.0],    # Top right
            [-1.0, 1.0, 1.0],   # Top left
            
            # Back face (z = -1.0)
            [-1.0, -1.0, -1.0], # Bottom left
            [1.0, -1.0, -1.0],  # Bottom right
            [1.0, 1.0, -1.0],   # Top right
            [-1.0, 1.0, -1.0],  # Top left
            
            # Right face (x = 1.0)
            [1.0, -1.0, -1.0],  # Bottom left
            [1.0, 1.0, -1.0],   # Top left
            [1.0, 1.0, 1.0],    # Top right
            [1.0, -1.0, 1.0],   # Bottom right
            
            # Left face (x = -1.0)
            [-1.0, -1.0, -1.0], # Bottom left
            [-1.0, -1.0, 1.0],  # Bottom right
            [-1.0, 1.0, 1.0],   # Top right
            [-1.0, 1.0, -1.0],  # Top left
            
            # Top face (y = 1.0)
            [-1.0, 1.0, -1.0],  # Bottom left
            [1.0, 1.0, -1.0],   # Bottom right
            [1.0, 1.0, 1.0],    # Top right
            [-1.0, 1.0, 1.0],   # Top left
            
            # Bottom face (y = -1.0)
            [-1.0, -1.0, -1.0], # Bottom left
            [1.0, -1.0, -1.0],  # Bottom right
            [1.0, -1.0, 1.0],   # Top right
            [-1.0, -1.0, 1.0],  # Top left
        ]
        
        # Texture coordinates for each vertex
        tex_coords = [
            [0.0, 0.0],
            [1.0, 0.0],
            [1.0, 1.0],
            [0.0, 1.0],
        ] * 6  # Repeat for each face
        
        # Define which vertices make up each face (as quads)
        face_indices = [
            [0, 1, 2, 3],       # Front face
            [4, 5, 6, 7],       # Back face
            [8, 9, 10, 11],     # Right face
            [12, 13, 14, 15],   # Left face
            [16, 17, 18, 19],   # Top face
            [20, 21, 22, 23],   # Bottom face
        ]
        
        # Draw the cube
        glEnable(GL_TEXTURE_2D)
        glColor3f(1.0, 1.0, 1.0)  # White to not affect texture colors
        
        for i, face in enumerate(face_indices):
            glBindTexture(GL_TEXTURE_2D, self.textures[i])
            
            glBegin(GL_QUADS)
            for j, vertex_idx in enumerate(face):
                glTexCoord2f(tex_coords[vertex_idx][0], tex_coords[vertex_idx][1])
                glVertex3f(vertices[vertex_idx][0], vertices[vertex_idx][1], vertices[vertex_idx][2])
            glEnd()
        
        glDisable(GL_TEXTURE_2D)
    
    def run(self):
        roll_active = False
        roll_start_time = 0
        roll_duration = 2  # seconds
        
        while True:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    pygame.quit()
                    return
                
                # Handle key presses
                if event.type == pygame.KEYDOWN:
                    if event.key == pygame.K_ESCAPE:
                        pygame.quit()
                        return
                    elif event.key == pygame.K_SPACE:
                        # Roll the dice
                        roll_active = True
                        roll_start_time = pygame.time.get_ticks() / 1000.0
                    elif event.key == pygame.K_r:
                        # Toggle auto-rotation
                        self.auto_rotate = not self.auto_rotate
            
            # Update rotation
            if self.auto_rotate and not roll_active:
                self.rotation[0] += 0.5
                self.rotation[1] += 0.7
                self.rotation[2] += 0.3
            
            # Handle dice rolling animation
            if roll_active:
                current_time = pygame.time.get_ticks() / 1000.0
                elapsed = current_time - roll_start_time
                
                if elapsed < roll_duration:
                    # Fast rotation that slows down over time
                    progress = elapsed / roll_duration
                    speed_factor = 1.0 - (progress * progress)  # Quadratic slowdown
                    
                    self.rotation[0] += 5.0 * speed_factor
                    self.rotation[1] += 7.0 * speed_factor
                    self.rotation[2] += 6.0 * speed_factor
                else:
                    # End of roll - snap to a random orientation
                    roll_active = False
                    self.rotation = [
                        90 * np.random.randint(0, 4),
                        90 * np.random.randint(0, 4),
                        90 * np.random.randint(0, 4)
                    ]
            
            # Clear the screen and depth buffer
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
            
            # Reset transformations
            glLoadIdentity()
            glTranslatef(0, 0, -5)
            
            # Apply rotations
            glRotatef(self.rotation[0], 1, 0, 0)
            glRotatef(self.rotation[1], 0, 1, 0)
            glRotatef(self.rotation[2], 0, 0, 1)
            
            # Draw the dice
            self.draw_dice()
            
            # Update the display
            pygame.display.flip()
            self.clock.tick(60)

if __name__ == "__main__":
    print("Elegant 3D Dice")
    print("Controls:")
    print("  Space: Roll the dice")
    print("  R: Toggle auto-rotation")
    print("  ESC: Exit")
    
    dice = ElegantDice()
    dice.run()