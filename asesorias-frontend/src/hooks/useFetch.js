import { useState, useCallback } from 'react';
import axios from '../api/axiosConfig';
import Swal from 'sweetalert2';

export const useFetch = (endpoint) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const get = useCallback(async (id = null) => {
    setLoading(true);
    setError(null);
    try {
      const url = id ? `${endpoint}/${id}` : endpoint;
      const response = await axios.get(url);
      // Maneja estructuras de respuesta { value: [...], Count: N } o array directo
      const data = response.data?.value || response.data;
      setData(data);
      return data;
    } catch (err) {
      const message = err.response?.data?.message || err.message;
      setError(message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, [endpoint]);

  const post = useCallback(async (payload) => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.post(endpoint, payload);
      setData(response.data);
      return response.data;
    } catch (err) {
      const message = err.response?.data?.message || err.message;
      setError(message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, [endpoint]);

  const put = useCallback(async (id, payload) => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.put(`${endpoint}/${id}`, payload);
      setData(response.data);
      return response.data;
    } catch (err) {
      const message = err.response?.data?.message || err.message;
      setError(message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, [endpoint]);

  const delete_ = useCallback(async (id) => {
    setLoading(true);
    setError(null);
    try {
      await axios.delete(`${endpoint}/${id}`);
      setData(null);
      return true;
    } catch (err) {
      const message = err.response?.data?.message || err.message;
      setError(message);
      throw err;
    } finally {
      setLoading(false);
    }
  }, [endpoint]);

  return { data, loading, error, get, post, put, delete: delete_ };
};
