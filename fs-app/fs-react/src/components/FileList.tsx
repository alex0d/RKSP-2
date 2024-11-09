import React from 'react';
import { Trash2, Download, ExternalLink } from 'lucide-react';
import { DownloadableFileDto } from '../types/file';
import { MULTIPART_ENDPOINT } from '../constants/api';

interface FileListProps {
    files: DownloadableFileDto[];
    onDelete: (id: string) => void;
}

const FileList: React.FC<FileListProps> = ({ files, onDelete }) => {
    return (
        <ul className="list-group">
            {files.map((file) => (
                <li key={file.id} className="list-group-item d-flex justify-content-between align-items-center">
                    <span>{file.filename} <small className="text-muted">({(file.sizeBytes / 1024).toFixed(2)} KB)</small></span>
                    <div className="btn-group" role="group">
                        <a
                            href={`${MULTIPART_ENDPOINT}/${file.id}`}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="btn btn-outline-primary btn-sm"
                            title="Open file"
                        >
                            <ExternalLink size={18} />
                        </a>
                        <a
                            href={`${MULTIPART_ENDPOINT}/download/${file.id}`}
                            download
                            className="btn btn-outline-success btn-sm"
                            title="Download file"
                        >
                            <Download size={18} />
                        </a>
                        <button
                            onClick={() => onDelete(file.id)}
                            className="btn btn-outline-danger btn-sm"
                            title="Delete file"
                        >
                            <Trash2 size={18} />
                        </button>
                    </div>
                </li>
            ))}
        </ul>
    );
};

export default FileList;