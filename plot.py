import os
import matplotlib.pyplot as plt

base_dir = 'histogram'

# Folder-specific cvar values
folder_cvar = {
    'hist-0_1': 'cvar=0.1',
    'hist-1': 'cvar=1',
    'hist-2': 'cvar=2'
}

# Traverse the directory structure
for root, dirs, files in os.walk(base_dir):
    for file in files:
        if file.endswith('_hist.csv'):
            file_path = os.path.join(root, file)
            print("Processing file:", file_path)
            
            # Extract the immediate subfolder name after 'histogram'
            relative_path = os.path.relpath(root, base_dir)
            folder_name = relative_path.split(os.sep)[0]  # Get the first folder after 'histogram'
            
            print("Processing:", folder_name)
            if folder_name not in folder_cvar:
                continue  # Skip if folder is not in the predefined list

            # Get the cvar for the folder
            cvar = folder_cvar.get(folder_name, 'cvar=unknown')
            
            # Create the title for the plot
            title_file = file.replace('_hist.csv', '')
            title = f"{title_file} {cvar}"
            
            # Read and parse the file
            with open(file_path, 'r', encoding='utf-8') as f:
                lines = f.readlines()

            # Skip comment lines
            data_lines = [line for line in lines if not line.strip().startswith('#') and line.strip()]

            bins = []
            heights = []

            for line in data_lines:
                parts = line.strip().split(';')
                lower = float(parts[0].replace(',', '.'))
                upper = float(parts[1].replace(',', '.'))
                height = float(parts[2].replace(',', '.').replace('E', 'e'))
                bins.append(lower)
                heights.append(height)

            bins.append(float(data_lines[-1].split(';')[1].replace(',', '.')))

            # Plotting
            plt.figure(figsize=(10, 6))
            plt.bar(bins[:-1], heights, width=[bins[i+1]-bins[i] for i in range(len(bins)-1)],
                    align='edge', edgecolor='black')
            plt.xlabel("Time (s)")
            plt.ylabel("Relative Frequency")
            plt.xticks(ticks=[x * 0.5 for x in range(int(bins[0] * 2), int(bins[-1] * 2) + 1)])
            plt.title(title)
            plt.grid(True, linestyle='--', alpha=0.6)
            plt.tight_layout()

            # Replace spaces with underscores and remove parentheses in the output filename
            output_file = f"{folder_name}_{file.replace('_hist.csv', '')}_histogram.png"
            output_file = output_file.replace(" ", "_").replace("(", "").replace(")", "")
            plt.savefig(output_file)
            plt.close()

print("Histograms generated and saved.")
