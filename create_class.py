import sys

# Helper script to speed up generating new ASTNode classes

def main(class_name):
    # use the template file to generate an empty class
    
    template_file_name = './AstNodeTemplate.java'
    output_file_name = './ast/' + class_name + '.java'

    with open(template_file_name, 'r') as template_file, open(output_file_name, 'w') as output_file:
        for line in template_file:
            formatted_line = line.replace('AstNodeTemplate', class_name)
            output_file.write(formatted_line)
    
    print ("Added skeleton for {}".format(output_file_name));


if __name__ == '__main__':
    if len(sys.argv) != 2:
        print ("Usage: python3 create_class.py <class_name>")
        print ("E.g.   python3 create_class.py MyAstNode")
    else:
        main(sys.argv[1])
