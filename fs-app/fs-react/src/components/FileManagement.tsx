import React, { useState, useEffect } from 'react';
import { DownloadableFileDto } from '../types/file';
import { fetchFiles, uploadFiles, deleteFile } from '../api/fileApi';
import DropZone from './DropZone';
import FileList from './FileList';

const FileManagement: React.FC = () => {
    const [files, setFiles] = useState<DownloadableFileDto[]>([]);
    const [uploadStatus, setUploadStatus] = useState<string | null>(null);

    useEffect(() => {
        loadFiles();
    }, []);

    useEffect(() => {
        if (uploadStatus && !uploadStatus.includes('Error')) {
            const timer = setTimeout(() => {
                setUploadStatus(null);
            }, 3000);

            return () => clearTimeout(timer);
        }
    }, [uploadStatus]);

    const loadFiles = async () => {
        try {
            const fetchedFiles = await fetchFiles();
            setFiles(fetchedFiles);
        } catch (error) {
            console.error('Error fetching files:', error);
        }
    };

    const onDrop = async (acceptedFiles: File[]) => {
        const formData = new FormData();
        acceptedFiles.forEach(file => {
            formData.append('files', file);
        });

        try {
            await uploadFiles(formData);
            setUploadStatus('Files uploaded successfully!');
            loadFiles();
        } catch (error) {
            console.error('Error uploading files:', error);
            setUploadStatus('Error uploading files.');
        }
    };

    const handleDelete = async (id: string) => {
        try {
            await deleteFile(id);
            loadFiles();
        } catch (error) {
            console.error('Error deleting file:', error);
        }
    };

    return (
        <div className="container-fluid">
            <div className="row">
                <div className="col-md-6 p-4">
                    <DropZone onDrop={onDrop} />
                    {uploadStatus && (
                        <div
                            className={`alert ${uploadStatus.includes('Error') ? 'alert-danger' : 'alert-success'} mt-3`}
                            role="alert"
                        >
                            {uploadStatus}
                        </div>
                    )}
                </div>
                <div className="col-md-6 p-4">
                    <h2 className="mb-4">Files</h2>
                    <FileList files={files} onDelete={handleDelete} />
                </div>
            </div>
        </div>
    );
};

export default FileManagement;