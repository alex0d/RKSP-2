import { MULTIPART_ENDPOINT } from '../constants/api';
import { DownloadableFileDto } from '../types/file';

export const fetchFiles = async (): Promise<DownloadableFileDto[]> => {
    const response = await fetch(MULTIPART_ENDPOINT);
    if (!response.ok) {
        throw new Error('Failed to fetch files');
    }
    return response.json();
};

export const uploadFiles = async (formData: FormData): Promise<void> => {
    const response = await fetch(MULTIPART_ENDPOINT, {
        method: 'POST',
        body: formData,
    });
    if (!response.ok) {
        throw new Error('Failed to upload files');
    }
};

export const deleteFile = async (id: string): Promise<void> => {
    const response = await fetch(`${MULTIPART_ENDPOINT}/${id}`, { method: 'DELETE' });
    if (!response.ok) {
        throw new Error('Failed to delete file');
    }
};