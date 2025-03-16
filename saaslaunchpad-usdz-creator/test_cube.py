import pygame
from pygame.locals import *
from OpenGL.GL import *
from OpenGL.GLU import *

def draw_cube():
    vertices = (
        (1, -1, -1), (1, 1, -1), (-1, 1, -1), (-1, -1, -1),
        (1, -1, 1), (1, 1, 1), (-1, -1, 1), (-1, 1, 1)
    )
    edges = (
        (0, 1), (1, 2), (2, 3), (3, 0),
        (4, 5), (5, 7), (7, 6), (6, 4),
        (0, 4), (1, 5), (2, 7), (3, 6)
    )
    
    glBegin(GL_LINES)
    for edge in edges:
        for vertex in edge:
            glVertex3fv(vertices[vertex])
    glEnd()

def main():
    pygame.init()
    display = (800, 600)
    pygame.display.set_mode(display, DOUBLEBUF | OPENGL)
    pygame.display.set_caption('Simple Cube')
    
    # Set background color (dark blue)
    glClearColor(0.05, 0.05, 0.2, 1.0)
    
    # Set up the perspective
    gluPerspective(45, (display[0] / display[1]), 0.1, 50.0)
    glTranslatef(0.0, 0.0, -5)
    
    # Set up basic OpenGL
    glEnable(GL_DEPTH_TEST)
    
    # Print OpenGL version for debugging
    print("OpenGL Version:", glGetString(GL_VERSION).decode())
    
    rotation = [0, 0, 0]
    clock = pygame.time.Clock()
    
    while True:
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                return
            if event.type == pygame.KEYDOWN:
                if event.key == pygame.K_ESCAPE:
                    pygame.quit()
                    return
        
        # Update rotation
        rotation[0] += 1
        rotation[1] += 1
        
        # Clear the screen
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
        
        # Reset transformations
        glLoadIdentity()
        glTranslatef(0, 0, -5)
        
        # Apply rotations
        glRotatef(rotation[0], 1, 0, 0)
        glRotatef(rotation[1], 0, 1, 0)
        
        # Set color to white
        glColor3f(1.0, 1.0, 1.0)
        
        # Draw the cube
        draw_cube()
        
        # Update the display
        pygame.display.flip()
        clock.tick(60)

if __name__ == "__main__":
    main()