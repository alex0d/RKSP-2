import React from 'react';
import { useDropzone } from 'react-dropzone';

interface DropZoneProps {
    onDrop: (acceptedFiles: File[]) => void;
}

const DropZone: React.FC<DropZoneProps> = ({ onDrop }) => {
    const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

    return (
        <div
            {...getRootProps()}
            className={`dropzone d-flex flex-column justify-content-center align-items-center p-5 border rounded ${
                isDragActive ? 'border-primary' : 'border-secondary'
            }`}
            style={{ minHeight: '300px', cursor: 'pointer' }}
        >
            <input {...getInputProps()} />
            <i className="bi bi-cloud-upload fs-1 mb-3"></i>
            {isDragActive ? (
                <p className="text-primary">Drop the files here ...</p>
            ) : (
                <p>Drag 'n' drop some files here, or click to select files</p>
            )}
        </div>
    );
};

export default DropZone;